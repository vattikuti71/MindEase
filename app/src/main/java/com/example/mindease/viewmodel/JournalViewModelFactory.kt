package com.example.mindease.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JournalViewModelFactory(
    // app context
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // check type
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // return vm
            return JournalViewModel(application) as T
        }
        // error throw
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}