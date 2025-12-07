package com.example.mindease.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindease.data.MoodDatabase
import com.example.mindease.data.mindfulness.MindfulnessExercise
import com.example.mindease.repository.MindfulnessRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MindfulnessViewModel(application: Application) : AndroidViewModel(application) {

    // local db
    private val db = MoodDatabase.getDatabase(application)
    private val repo = MindfulnessRepository(db.mindfulnessDao(), FirebaseFirestore.getInstance())

    val exercises: StateFlow<List<MindfulnessExercise>> =
        repo.localExercisesFlow().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            repo.populateDummyIfEmpty()
            syncFromCloud()
        }
    }

    // Sync data
    fun syncFromCloud() {
        viewModelScope.launch {
            repo.syncFromCloud()
        }
    }

    // mark play
    fun markPlayed(id: Int) {
        viewModelScope.launch {
            repo.markPlayed(id)
        }
    }

    // Update played time
    fun addPlayedTime(id: Int, seconds: Int) {
        viewModelScope.launch {
            repo.addPlayedTime(id, seconds) // add time
        }
    }
}