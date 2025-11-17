package com.example.mindease.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindease.data.MoodDatabase
import com.example.mindease.repository.MoodRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(navController: NavController) {

    val dao = MoodDatabase.getDatabase(navController.context).moodDao()
    val repository = MoodRepository(dao)

    // Collect Mood Flow
    val moods by repository.getAllMoods().collectAsState(initial = emptyList())

    val auth = FirebaseAuth.getInstance()
    val username = auth.currentUser?.displayName
        ?: auth.currentUser?.email?.substringBefore("@")
        ?: "User"

    val lastMood = moods.lastOrNull()
    val moodLabel = lastMood?.moodType ?: "No mood logged"
    val moodRating = lastMood?.moodRating ?: 0

    val moodColor = if (moodRating >= 3) Color(0xFF4CAF50) else Color(0xFFFF5252)

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE0F2FF), Color.White)
                    )
                )
                .padding(20.dp)
        ) {

            // HEADER + LOGOUT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, $username 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "How are you feeling today?",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = Color.Red,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LAST MOOD SUMMARY
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Last Mood: $moodLabel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = moodColor
                    )

                    if (lastMood != null) {
                        Text(
                            text = "Rating: ${lastMood.moodRating}/5",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = moodColor
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("moodTracker") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFF64B5F6))
                    ) {
                        Text("Quick Mood Check →", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TODAY'S MOOD SECTION
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(Color(0xFFF5F7FE))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Today's Mood",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = moodLabel,
                        fontSize = 20.sp,
                        color = moodColor,
                        fontWeight = FontWeight.Bold
                    )

                    if (lastMood != null) {
                        Text(
                            text = "Rating: ${lastMood.moodRating}/5",
                            fontSize = 18.sp,
                            color = moodColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // QUICK ACTIONS
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            QuickActionGrid(navController)

            Spacer(modifier = Modifier.height(100.dp))
        }

        // BOTTOM NAV BAR
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavBar(navController)
        }
    }
}

//////////////////////////////////////////////////////
// QUICK ACTION GRID + CARDS
//////////////////////////////////////////////////////

@Composable
fun QuickActionGrid(navController: NavController) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
            QuickActionCard("Mood Tracker") { navController.navigate("moodTracker") }
            QuickActionCard("Habits") { }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
            QuickActionCard("Habit Tracker") { }
            QuickActionCard("Community") { }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
            QuickActionCard("Mindfulness") { }
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
            QuickActionCard("AI Insights") { }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun QuickActionCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

//////////////////////////////////////////////////////
// BOTTOM NAV BAR
//////////////////////////////////////////////////////

@Composable
fun BottomNavBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(Icons.Default.Home, "Home") { navController.navigate("home") }
        NavItem(Icons.Default.EmojiEmotions, "Mood") { navController.navigate("moodTracker") }
        NavItem(Icons.Default.Lightbulb, "AI Insights") { navController.navigate("aiInsights") }
        NavItem(Icons.Default.Person, "Profile") { navController.navigate("profile") }
    }
}

@Composable
fun NavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(icon, contentDescription = label, tint = Color(0xFF8E8E8E))
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}
