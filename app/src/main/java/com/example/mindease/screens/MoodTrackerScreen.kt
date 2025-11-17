package com.example.mindease.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mindease.data.Mood
import com.example.mindease.repository.MoodRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTrackerScreen(repository: MoodRepository, navController: NavController) {

    val coroutineScope = rememberCoroutineScope()

    var selectedMood by remember { mutableStateOf(3) }
    var selectedMoodType by remember { mutableStateOf("Neutral") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val moodTypes = listOf("Happy", "Calm", "Neutral", "Sad", "Stressed")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.titleLarge
        )

        // Rating Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            (1..5).forEach { rating ->
                Button(
                    onClick = { selectedMood = rating },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (selectedMood == rating) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(rating.toString())
                }
            }
        }

        // Mood Type Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = selectedMoodType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Mood Type") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                moodTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedMoodType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        // Notes
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Optional Note") },
            modifier = Modifier.fillMaxWidth()
        )

        // Save Button
        Button(
            onClick = {
                val mood = Mood(
                    moodRating = selectedMood,
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Mood")
        }
    }
}
