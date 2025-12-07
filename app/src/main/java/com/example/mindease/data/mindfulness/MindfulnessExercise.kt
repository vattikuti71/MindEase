package com.example.mindease.data.mindfulness

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mindfulness_exercises")
data class MindfulnessExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val remoteId: String? = null,
    val title: String,
    val description: String? = null,
    val audioUrl: String? = null,
    val durationSeconds: Int = 0,
    val lastPlayedAt: Long? = null,
    val cachedLocally: Boolean = false,
    val durationPlayed: Int? = 0
)