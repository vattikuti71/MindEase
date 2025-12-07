package com.example.mindease.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindease.data.MoodDatabase
import com.example.mindease.data.Mood
import com.example.mindease.data.mindfulness.MindfulnessExercise
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoodDatabase.getDatabase(application)
    // Mood DAO
    private val moodDao = db.moodDao()
    // Journal DAO
    private val journalDao = db.journalDao()
    // Mindfulness DAO
    private val mindfulnessDao = db.mindfulnessDao()

    // Weekly mood averages
    val weeklyAverages: StateFlow<List<Float>> = moodDao.getAllMoods()
        .map { list ->
            val now = System.currentTimeMillis() // Current time
            val dayMillis = 24 * 60 * 60 * 1000L // One day
            (0..6).map { d ->
                val start = now - (d + 1) * dayMillis
                val end = now - d * dayMillis
                val values = list.filter { it.timestamp in start..end }.map { it.moodRating.toFloat() } // Mood values
                if (values.isEmpty()) 0f else values.average().toFloat() // Daily average
            }.reversed()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, List(7) { 0f })

    // Total mindfulness time in seconds
    val totalMindfulnessTime: StateFlow<Int> = mindfulnessDao.getAll()
        .map { list -> list.sumOf { it.durationPlayed ?: 0 } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
    // Journal entries
    val journalCountFlow = journalDao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}