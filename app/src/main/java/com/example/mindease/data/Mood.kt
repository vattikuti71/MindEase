package com.example.mindease.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_table")
data class Mood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val moodRating: Int,
    val moodType: String,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
