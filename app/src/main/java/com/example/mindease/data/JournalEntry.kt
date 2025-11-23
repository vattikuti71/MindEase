package com.example.mindease.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_table")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    // 0 = not synced, 1 = synced
    val syncStatus: Int = 0,

    // Firestore document id
    val remoteId: String? = null
)