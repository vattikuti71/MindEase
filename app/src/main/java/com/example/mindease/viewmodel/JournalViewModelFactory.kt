package com.example.mindease.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JournalViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
