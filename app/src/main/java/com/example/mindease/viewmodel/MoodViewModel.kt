package com.example.mindease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindease.data.Mood
import com.example.mindease.repository.MoodRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoodViewModel(private val repository: MoodRepository) : ViewModel() {

    // last mood
    private val _lastMood = MutableStateFlow<Mood?>(null)
    val lastMood: StateFlow<Mood?> = _lastMood

    // today mood
    private val _todayMood = MutableStateFlow<Mood?>(null)
    val todayMood: StateFlow<Mood?> = _todayMood

    init {
        loadMoods()          // initial load
        startAutoRefresh()   // auto refresh
    }

    private fun loadMoods() {
        viewModelScope.launch {
            _todayMood.value = repository.getTodaysMood()   // fetch today
            _lastMood.value = repository.getPreviousMood()  // fetch last
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(20 * 1000L) // 20 sec delay
                loadMoods()       // reload moods
            }
        }
    }
}