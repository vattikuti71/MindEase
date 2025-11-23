package com.example.mindease.sync

import android.content.Context
import com.example.mindease.data.JournalEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class JournalRemoteDataSource(private val context: Context) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth get() = FirebaseAuth.getInstance()

    private fun collectionRef() =
        firestore.collection("users")
            .document(auth.currentUser?.uid ?: "ANONYMOUS")
            .collection("journals")

    suspend fun uploadEntry(entry: JournalEntry): String {
        val data = mapOf(
            "title" to entry.title,
            "content" to entry.content,
            "updatedAt" to entry.updatedAt,
            "createdAt" to entry.createdAt
        )

        return if (entry.remoteId != null) {
            collectionRef().document(entry.remoteId).set(data).await()
            entry.remoteId
        } else {
            val doc = collectionRef().add(data).await()
            doc.id
        }
    }

    suspend fun deleteRemote(remoteId: String?) {
        if (remoteId == null) return
        collectionRef().document(remoteId).delete().await()
    }

    suspend fun fetchAllRemote(): List<JournalEntry> {
        val snapshot = collectionRef().get().await()
        return snapshot.documents.map { doc ->
            JournalEntry(
                remoteId = doc.id,
                title = doc.getString("title") ?: "",
                content = doc.getString("content") ?: "",
                updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis(),
                createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                syncStatus = 1
            )
        }
    }
}