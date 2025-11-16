package com.example.mindease

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindease.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        // Splash Screen
        composable("splash") { SplashScreen(navController) }

        // Authentication Screens
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }

        // HomeScreen
        composable("home") { HomeScreen(navController) }
    }
}