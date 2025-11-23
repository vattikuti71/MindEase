package com.example.mindease.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntry): Long

    @Update
    suspend fun update(entry: JournalEntry)

    @Delete
    suspend fun delete(entry: JournalEntry)

    @Query("SELECT * FROM journal_table WHERE syncStatus != 2 ORDER BY updatedAt DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_table WHERE id = :id")
    suspend fun getEntryById(id: Int): JournalEntry?

    @Query("UPDATE journal_table SET syncStatus = :status, remoteId = :remoteId WHERE id = :localId")
    suspend fun markSyncStatus(localId: Int, status: Int, remoteId: String?)

    @Query("SELECT * FROM journal_table WHERE syncStatus = 0")
    suspend fun getUnsyncedEntries(): List<JournalEntry>
}