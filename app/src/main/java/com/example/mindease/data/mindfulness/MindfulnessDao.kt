package com.example.mindease.data.mindfulness

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MindfulnessDao {

    // List all exercises
    @Query("SELECT * FROM mindfulness_exercises ORDER BY id ASC")
    fun getAll(): Flow<List<MindfulnessExercise>> // Flow all exercises

    // List all exercises
    @Query("SELECT * FROM mindfulness_exercises ORDER BY id ASC")
    suspend fun getAllList(): List<MindfulnessExercise>

    // Fetch by ID
    @Query("SELECT * FROM mindfulness_exercises WHERE id = :id")
    suspend fun getById(id: Int): MindfulnessExercise?

    // Insert multiple items
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MindfulnessExercise>)

    // Insert single item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MindfulnessExercise)

    @Query("""
        UPDATE mindfulness_exercises
        SET durationPlayed = IFNULL(durationPlayed, 0) + :seconds,
            lastPlayedAt = :timestamp
        WHERE id = :id
    """)

    // Update played time
    suspend fun addPlayedTime(id: Int, seconds: Int, timestamp: Long)
}