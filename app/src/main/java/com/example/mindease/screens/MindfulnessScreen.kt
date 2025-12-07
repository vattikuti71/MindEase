package com.example.mindease.screens

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mindease.data.mindfulness.MindfulnessExercise
import com.example.mindease.notifications.NotificationUtils
import com.example.mindease.viewmodel.MindfulnessViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindfulnessScreen(navController: NavController, viewModel: MindfulnessViewModel = viewModel()) {

    // observe exercises
    val exercises by viewModel.exercises.collectAsState()
    // scope
    val coroutineScope = rememberCoroutineScope()
    // context
    val context = LocalContext.current

    var currentlyPlayingId by remember { mutableStateOf<Int?>(null) } // current id
    var remainingTime by remember { mutableStateOf(0) } // timer
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) } // player

    LaunchedEffect(Unit) { viewModel.syncFromCloud() } // sync cloud

    LaunchedEffect(currentlyPlayingId) { // timer effect
        while (currentlyPlayingId != null && remainingTime > 0) {
            delay(1000) // wait
            remainingTime -= 1 // countdown
        }

        currentlyPlayingId?.let { id -> // finished
            val playedSeconds = exercises.find { it.id == id }?.durationSeconds?.minus(remainingTime) ?: 0 // calc played
            if (playedSeconds > 0) {
                coroutineScope.launch { viewModel.addPlayedTime(id, playedSeconds) } // save time
                NotificationUtils.sendNotification(
                    context,
                    "Mindfulness Completed",
                    "You completed $playedSeconds seconds of '${exercises.find { it.id == id }?.title}'" // notify
                )
            }
        }

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentlyPlayingId = null
        remainingTime = 0
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            // Top Bar
            TopAppBar(
                title = { Text("Mindfulness Exercises", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3)),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // list
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(exercises) { ex ->
                    MindfulnessItem(
                        item = ex,
                        isPlaying = ex.id == currentlyPlayingId, // check
                        remainingTime = if (ex.id == currentlyPlayingId) remainingTime else ex.durationSeconds, // timer
                        onPlay = { id, audioUrl, duration ->
                            if (currentlyPlayingId == id) {
                                val played = duration - remainingTime
                                if (played > 0) {
                                    coroutineScope.launch { viewModel.addPlayedTime(id, played) }
                                    NotificationUtils.sendNotification(
                                        context,
                                        "Mindfulness Completed",
                                        "You completed $played seconds of '${ex.title}'"
                                    )
                                }

                                mediaPlayer?.stop() // stop
                                mediaPlayer?.release() // release
                                mediaPlayer = null
                                currentlyPlayingId = null // reset
                                remainingTime = 0
                            } else { // play new
                                mediaPlayer?.stop()
                                mediaPlayer?.release()

                                currentlyPlayingId = id // set id
                                remainingTime = duration // set timer

                                audioUrl?.let {
                                    try {
                                        mediaPlayer = MediaPlayer().apply { // init
                                            setDataSource(it) // set source
                                            prepare() // prepare
                                            start() // start
                                            setOnCompletionListener {
                                                // save
                                                coroutineScope.launch { viewModel.addPlayedTime(id, duration) }
                                                NotificationUtils.sendNotification(
                                                    context,
                                                    "Mindfulness Completed",
                                                    "You completed $duration seconds of '${ex.title}'"
                                                )
                                                currentlyPlayingId = null
                                                mediaPlayer?.release()
                                                mediaPlayer = null
                                                remainingTime = 0
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        currentlyPlayingId = null
                                        remainingTime = 0
                                    }
                                }
                            }
                            // mark
                            viewModel.markPlayed(id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MindfulnessItem(
    item: MindfulnessExercise,
    isPlaying: Boolean,
    remainingTime: Int,
    onPlay: (Int, String?, Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // title
            Text(item.title, style = MaterialTheme.typography.titleMedium, color = Color(0xFF0D47A1))
            Text(item.description ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray, maxLines = 3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (isPlaying) "Time left: ${remainingTime}s"
                    else "Duration: ${item.durationSeconds}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                FilledIconButton(
                    onClick = { onPlay(item.id, item.audioUrl, item.durationSeconds) },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isPlaying) Color(0xFFD32F2F) else Color(0xFF0D47A1),
                        contentColor = Color.White
                    )
                ) {
                    // icon
                    Icon(if (isPlaying) Icons.Filled.Stop else Icons.Filled.PlayArrow, contentDescription = null)
                }
            }
        }
    }
}