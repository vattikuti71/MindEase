package com.example.mindease.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindease.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFB3E5FC), Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "MindEase Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MindEase",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Breathe. Reflect. Grow.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0288D1)
            )
        }
    }

    // FIXED LOGIN CHECK
    LaunchedEffect(Unit) {
        delay(2000)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // User already logged in → Go Home
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            // User NOT logged in → Go Login
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
}
