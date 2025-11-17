package com.example.mindease.repository

import com.example.mindease.data.Mood
import com.example.mindease.data.MoodDao
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val dao: MoodDao) {

    /** Insert mood into database */
    suspend fun addMood(mood: Mood) {
        dao.insertMood(mood)
    }

    /** Observe moods in real-time (Flow) */
    fun getAllMoods(): Flow<List<Mood>> {
        return dao.getAllMoods()
    }
}
