package com.example.mindease.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindease.data.MoodDatabase
import com.example.mindease.data.JournalEntry
import com.example.mindease.repository.JournalRepository
import com.example.mindease.sync.JournalRemoteDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoodDatabase.getDatabase(application)
    private val journalDao = db.journalDao()
    private val remote = JournalRemoteDataSource(application.applicationContext)
    private val repository = JournalRepository(journalDao, remote)

    val allEntries: StateFlow<List<JournalEntry>> =
        repository.getAllEntries()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addEntry(title: String, content: String) = viewModelScope.launch {
        val entry = JournalEntry(title = title, content = content)
        repository.addEntry(entry)
    }

    fun updateEntry(entry: JournalEntry) = viewModelScope.launch {
        repository.updateEntry(entry)
    }

    fun deleteEntry(entry: JournalEntry) = viewModelScope.launch {
        repository.deleteEntry(entry)
    }

    suspend fun getEntryById(id: Int): JournalEntry? {
        return repository.getEntryById(id)
    }
}
