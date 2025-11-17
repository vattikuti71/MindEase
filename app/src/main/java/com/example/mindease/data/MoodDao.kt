package com.example.mindease.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    //Insert mood entry
    @Insert
    suspend fun insertMood(mood: Mood)

    // Observe all saved moods
    @Query("SELECT * FROM mood_table ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<Mood>>
}