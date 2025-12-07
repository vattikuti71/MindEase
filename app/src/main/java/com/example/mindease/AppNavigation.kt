package com.example.mindease

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindease.data.MoodDatabase
import com.example.mindease.repository.MoodRepository
import com.example.mindease.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val db = MoodDatabase.getDatabase(context)
    val moodRepository = MoodRepository(db.moodDao())

    NavHost(navController = navController, startDestination = "splash") {

        // Auth screens
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }

        // Home screen
        composable("home") { HomeScreen(navController) }

        // Mood screen
        composable("moodTracker") {
            MoodTrackerScreen(repository = moodRepository, navController = navController)
        }

        // Journal list screen
        composable("journalList") { JournalListScreen(navController) }

        // Journal create screen
        composable("journalCreate") {
            JournalEditorScreen(navController, entryId = null)
        }

        // Journal detail screen
        composable("journalDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            JournalEditorScreen(navController, entryId = id)
        }

        // Mind screen
        composable("mindfulness") { MindfulnessScreen(navController) }

        // Progress screen
        composable("progress") { ProgressScreen(navController) }

        // Settings screen
        composable("settings") { SettingsScreen(navController) }
    }
}