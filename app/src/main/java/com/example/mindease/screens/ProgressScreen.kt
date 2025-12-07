package com.example.mindease.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mindease.viewmodel.ProgressViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(navController: NavController, vm: ProgressViewModel = viewModel()) {

    // weekly data
    val weekly by vm.weeklyAverages.collectAsState()
    // mindfulness
    val totalMindfulness by vm.totalMindfulnessTime.collectAsState()
    // journal
    val journalCount by vm.journalCountFlow.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            // TopBar
            TopAppBar(
                title = { Text("Progress Insights", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3)),
                navigationIcon = {
                    // back arrow
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
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Weekly Mood Bar Chart
            Text("Weekly Mood (older → newer)", style = MaterialTheme.typography.titleMedium)
            WeeklyBarChartInteractive(weekly)

            // Mindfulness Pie Chart
            Text("Mindfulness Exercises", style = MaterialTheme.typography.titleMedium)
            MindfulnessPieChartInteractive(totalMindfulness)

            // Journal Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    // navigate
                    .clickable { navController.navigate("journalList") },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                // content
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // title
                    Text("Journaling", style = MaterialTheme.typography.titleMedium, color = Color(0xFF0D47A1))
                    // count
                    Text("Entries: ${journalCount.size}", style = MaterialTheme.typography.bodyMedium)
                    // hint
                    Text("Tap to view all entries", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun MindfulnessPieChartInteractive(totalSeconds: Int) {
    val totalMinutes = totalSeconds / 60f // minutes
    val usedPortion = totalMinutes.coerceAtMost(60f) // used
    val remainingPortion = 60f - usedPortion // remaining

    // toggle
    var showDetails by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                // toggle tap
                .pointerInput(Unit) { detectTapGestures { showDetails = !showDetails } }
        ) {
            drawArc(
                color = Color(0xFF64B5F6),
                startAngle = -90f,
                sweepAngle = (usedPortion / 60f) * 360f, // angle
                useCenter = true
            )
            drawArc(
                color = Color.LightGray,
                startAngle = -90f + (usedPortion / 60f) * 360f,
                sweepAngle = (remainingPortion / 60f) * 360f,
                useCenter = true
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total mindfulness: ${totalMinutes.toInt()} min")
        if (showDetails) {
            Text(
                // details
                "Used: ${usedPortion.toInt()} min, Remaining: ${remainingPortion.toInt()} min",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun WeeklyBarChartInteractive(values: List<Float>) {
    val max = (values.maxOrNull() ?: 5f).coerceAtLeast(5f)
    // hover
    var hoveredIndex by remember { mutableStateOf(-1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        horizontalArrangement = Arrangement.SpaceBetween // spacing
    ) {
        values.forEachIndexed { index, v ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(28.dp) // width
                        .height(140.dp) // height
                        .background(Color(0xFFEDEDED))
                        .pointerInput(Unit) {
                            detectTapGestures { hoveredIndex = if (hoveredIndex == index) -1 else index } // hover
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barHeight = (v / max) * size.height // bar height
                        drawRect(
                            color = Color(0xFF64B5F6),
                            topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - barHeight),
                            size = Size(size.width, barHeight)
                        )
                    }
                    if (hoveredIndex == index) {
                        Text(
                            text = String.format("%.1f", v),
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp)) // spacing
                // label
                Text(
                    (v).let { if (it == 0f) "-" else String.format("%.1f", it) },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}