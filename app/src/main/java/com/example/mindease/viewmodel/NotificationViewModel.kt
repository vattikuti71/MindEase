package com.example.mindease.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Notification data
data class Notification(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val body: String,
    val type: Type,
    val targetId: Int? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    enum class Type { JOURNAL, MINDFULNESS, OTHER }
}

class NotificationViewModel : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    /** Add new notification to top */
    fun addNotification(notification: Notification) {
        _notifications.value = listOf(notification) + _notifications.value // add top
    }

    /** Remove by id */
    fun removeNotification(id: Long) {
        _notifications.value = _notifications.value.filter { it.id != id } // remove match
    }

    /** Clear all notifications */
    fun clearAll() {
        _notifications.value = emptyList() // empty list
    }
}