package com.example.mindease.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindease.data.Mood
import com.example.mindease.repository.MoodRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTrackerScreen(repository: MoodRepository, navController: NavController) {

    val coroutineScope = rememberCoroutineScope()

    var selectedMoodIndex by remember { mutableStateOf(2) }
    var selectedMoodType by remember { mutableStateOf("Neutral") }
    var moodRating by remember { mutableStateOf(3) }
    var note by remember { mutableStateOf("") }

    val moodTypes = listOf("Happy", "Calm", "Neutral", "Sad", "Stress")
    val moodEmojis = listOf("😄", "😌", "😐", "😔", "😫")

    Scaffold(
        containerColor = Color.White,
        topBar = {
            // Blue Color Top Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Mood Tracker",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home") { popUpTo("home") { inclusive = true } }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E1E1E)
                )
            )

            //  Emoji Section
            Text("Select your mood", fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                moodEmojis.forEachIndexed { index, emoji ->

                    val isSelected = selectedMoodIndex == index
                    var scale by remember { mutableStateOf(1f) }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedMoodIndex = index
                                selectedMoodType = moodTypes[index]
                                scale = 1.2f
                            }
                            .scale(scale)
                    ) {
                        Text(
                            text = emoji,
                            fontSize = if (isSelected) 42.sp else 36.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = moodTypes[index],
                            fontSize = 14.sp,
                            color = if (isSelected) Color(0xFF1976D2) else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Mood Rating
            Text("How strongly do you feel this mood?", fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                (1..5).forEach { rating ->
                    Button(
                        onClick = { moodRating = rating },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (moodRating == rating) Color(0xFF64B5F6)
                                else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            rating.toString(),
                            color = if (moodRating == rating) Color.White else Color.Black
                        )
                    }
                }
            }

            // Note
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Optional Note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // Save Mood Button
            Button(
                onClick = {
                    val mood = Mood(
                        moodRating = moodRating,
                        moodType = selectedMoodType,
                        note = note
                    )

                    coroutineScope.launch {
                        repository.addMood(mood)
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save Mood", fontSize = 16.sp)
            }
        }
    }
}