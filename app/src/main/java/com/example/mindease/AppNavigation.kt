package com.example.mindease

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindease.data.MoodDatabase
import com.example.mindease.repository.MoodRepository
import com.example.mindease.screens.*
import com.example.mindease.viewmodel.JournalViewModel
import com.example.mindease.viewmodel.JournalViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import android.app.Application

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Create Room database and repository
    val context = LocalContext.current
    val db = MoodDatabase.getDatabase(context)
    val moodRepo = MoodRepository(db.moodDao())

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }

        composable("home") {
            HomeScreen(navController)
        }

        composable("moodTracker") {
            MoodTrackerScreen(moodRepo, navController)
        }

        composable("journalList") {
            JournalListScreen(navController)
        }

        composable("journalCreate") {
            JournalEditorScreen(navController, entryId = null)
        }

        composable("journalDetail/{id}") { backStack ->
            val entryId = backStack.arguments?.getString("id")?.toIntOrNull()
            JournalEditorScreen(navController, entryId = entryId)
        }
    }
}