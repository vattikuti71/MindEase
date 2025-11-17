package com.example.mindease.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    /** Insert a mood entry */
    @Insert
    suspend fun insertMood(mood: Mood)

    /** Observe all saved moods (auto-updates Home Screen) */
    @Query("SELECT * FROM mood_table ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<Mood>>
}
