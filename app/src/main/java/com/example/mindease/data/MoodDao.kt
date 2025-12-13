package com.example.mindease.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    // Insert new mood entry
    @Insert
    suspend fun insertMood(mood: Mood)

    // Observe all moods in descending order
    @Query("SELECT * FROM mood_table ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<Mood>>

    // Get latest mood
    @Query("SELECT * FROM mood_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMood(): Mood?

    // Get latest mood for today
    @Query("""
        SELECT * FROM mood_table 
        WHERE date(timestamp / 1000, 'unixepoch') = date('now') 
        ORDER BY timestamp DESC LIMIT 1
    """)
    suspend fun getTodaysMood(): Mood?

    // Get second latest mood
    @Query("SELECT * FROM mood_table ORDER BY timestamp DESC LIMIT 1 OFFSET 1")
    suspend fun getPreviousMood(): Mood?
}