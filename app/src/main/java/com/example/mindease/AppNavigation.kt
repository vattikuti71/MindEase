package com.example.mindease

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindease.data.MoodDatabase
import com.example.mindease.repository.MoodRepository
import com.example.mindease.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Create Room database and repository
    val context = androidx.compose.ui.platform.LocalContext.current
    val dao = MoodDatabase.getDatabase(context).moodDao()
    val repository = MoodRepository(dao)

    NavHost(navController = navController, startDestination = "splash") {

        // Splash Screen
        composable("splash") {
            SplashScreen(navController)
        }

        // Authentication Screens
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }

        // Home Screen
        composable("home") {
            HomeScreen(navController)
        }

        // Mood Tracker Screen
        composable("moodTracker") {
            MoodTrackerScreen(repository, navController)
        }
    }
}