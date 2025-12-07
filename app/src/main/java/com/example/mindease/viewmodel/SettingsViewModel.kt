package com.example.mindease.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindease.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val repo = SettingsRepository(application.applicationContext)

    // Observe notifications
    val notificationsEnabled: StateFlow<Boolean> =
        repo.notificationsEnabled.stateIn(viewModelScope, SharingStarted.Lazily, true)

    // Update notifications
    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { repo.setNotificationsEnabled(enabled) }
    }
}