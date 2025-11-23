package com.example.mindease.repository

import com.example.mindease.data.JournalDao
import com.example.mindease.data.JournalEntry
import com.example.mindease.sync.JournalRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow

class JournalRepository(
    private val dao: JournalDao,
    private val remote: JournalRemoteDataSource
) {

    fun getAllEntries(): Flow<List<JournalEntry>> = dao.getAllEntries()

    suspend fun getEntryById(id: Int): JournalEntry? = dao.getEntryById(id)

    suspend fun addEntry(entry: JournalEntry): Long = withContext(Dispatchers.IO) {
        val id = dao.insert(entry.copy(syncStatus = 0))
        // Fetch freshly inserted local row
        val localEntry = dao.getEntryById(id.toInt())
        localEntry?.let {
            try {
                val remoteId = remote.uploadEntry(it)
                dao.markSyncStatus(it.id, 1, remoteId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        id
    }

    suspend fun updateEntry(entry: JournalEntry) = withContext(Dispatchers.IO) {
        val updated = entry.copy(updatedAt = System.currentTimeMillis(), syncStatus = 0)
        dao.update(updated)
        try {
            val remoteId = remote.uploadEntry(updated)
            dao.markSyncStatus(updated.id, 1, remoteId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteEntry(entry: JournalEntry) = withContext(Dispatchers.IO) {
        // delete locally first
        dao.delete(entry)
        // try to delete remotely if remoteId exists
        try {
            remote.deleteRemote(entry.remoteId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}