package com.example.mindease.repository

import com.example.mindease.data.mindfulness.MindfulnessDao
import com.example.mindease.data.mindfulness.MindfulnessExercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class MindfulnessRepository(
    // local dao
    private val dao: MindfulnessDao,
    // cloud db
    private val firestore: FirebaseFirestore
) {

    private val collection = firestore.collection("mindfulness_exercises")

    // flow list
    fun localExercisesFlow(): Flow<List<MindfulnessExercise>> = dao.getAll()

    // cloud sync
    suspend fun syncFromCloud() {
        try {
            val snapshot = collection.get().await()
            val list = snapshot.documents.map { doc ->
                MindfulnessExercise(
                    id = 0,
                    remoteId = doc.id,
                    title = doc.getString("title") ?: "Untitled",
                    description = doc.getString("description") ?: "",
                    audioUrl = doc.getString("audioUrl"),
                    durationSeconds = (doc.getLong("durationSeconds") ?: 0L).toInt(),
                    cachedLocally = false,
                    durationPlayed = 0
                )
            }
            dao.insertAll(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // mark played
    suspend fun markPlayed(id: Int) {
        val timestamp = System.currentTimeMillis()
        dao.addPlayedTime(id, 0, timestamp)
    }

    // add time
    suspend fun addPlayedTime(id: Int, seconds: Int) {
        val timestamp = System.currentTimeMillis()
        dao.addPlayedTime(id, seconds, timestamp)
    }

    // Static Data
    suspend fun populateDummyIfEmpty() {
        val list = dao.getAllList()
        if (list.isEmpty()) {
            dao.insertAll(
                listOf(
                    // Static data 1
                    MindfulnessExercise(
                        title = "Breathing",
                        description = "Take deep, slow breaths to relax",
                        durationSeconds = 60,
                        durationPlayed = 0
                    ),
                    // Static data 2
                    MindfulnessExercise(
                        title = "Body Scan",
                        description = "Focus attention on different parts of your body",
                        durationSeconds = 120,
                        durationPlayed = 0
                    ),
                    // Static data 3
                    MindfulnessExercise(
                        title = "Mindful Walking",
                        description = "Walk slowly and notice your surroundings",
                        durationSeconds = 180,
                        durationPlayed = 0
                    )
                )
            )
        }
    }
}