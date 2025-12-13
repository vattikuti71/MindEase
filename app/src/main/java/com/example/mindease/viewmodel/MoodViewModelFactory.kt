package com.example.mindease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mindease.repository.MoodRepository

// ViewModel factory
class MoodViewModelFactory(
    private val repository: MoodRepository // repo instance
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // check class
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            return MoodViewModel(repository) as T // create Viewmodel
        }

        // unknown Viewmodel
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}