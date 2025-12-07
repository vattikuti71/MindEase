package com.example.mindease.repository

import android.content.Context
import com.example.mindease.settings.UserPreferencesDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepository(context: Context) {
    private val prefs = UserPreferencesDataStore(context)
    val notificationsEnabled: Flow<Boolean> = prefs.notificationsEnabledFlow
    suspend fun setNotificationsEnabled(enabled: Boolean) = prefs.setNotificationsEnabled(enabled)
}