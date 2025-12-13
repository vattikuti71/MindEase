package com.example.mindease.repository

import com.example.mindease.data.Mood
import com.example.mindease.data.MoodDao
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val dao: MoodDao) {

    // Insert new mood
    suspend fun addMood(mood: Mood) {
        dao.insertMood(mood)
    }

    // Observe all moods
    fun getAllMoods(): Flow<List<Mood>> {
        return dao.getAllMoods()
    }

    // Get latest mood
    suspend fun getLatestMood(): Mood? {
        return dao.getLatestMood()
    }

    // Get today's latest mood
    suspend fun getTodaysMood(): Mood? {
        return dao.getTodaysMood()
    }

    // Get previous mood
    suspend fun getPreviousMood(): Mood? {
        return dao.getPreviousMood()
    }
}