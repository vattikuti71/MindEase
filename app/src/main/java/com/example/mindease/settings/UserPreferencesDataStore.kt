package com.example.mindease.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataStore(private val context: Context) {

    // notification key
    private val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")

    // data flow
    val notificationsEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[NOTIFICATIONS] ?: true }
    // set notification
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[NOTIFICATIONS] = enabled }
    }
}