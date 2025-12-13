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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindease.repository.MoodRepository
import com.example.mindease.data.MoodDatabase
import com.example.mindease.viewmodel.MoodViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindease.viewmodel.MoodViewModelFactory

@Composable
fun HomeScreen(
    navController: NavController,
    moodViewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(
            MoodRepository(MoodDatabase.getDatabase(LocalContext.current).moodDao())
        )
    )
) {
    val lastMood by moodViewModel.lastMood.collectAsState()
    val todayMood by moodViewModel.todayMood.collectAsState()

    // username
    val username = FirebaseAuth.getInstance().currentUser?.displayName
        ?: FirebaseAuth.getInstance().currentUser?.email?.substringBefore("@")
        ?: "User"

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFE0F2FF), Color.White)))
        )

        Column(modifier = Modifier.fillMaxSize()) {

            TopBar(username, navController)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                // Last Mood Card
                LastMoodCard(
                    moodLabel = lastMood?.moodType ?: "No mood logged",
                    moodRating = lastMood?.moodRating ?: 0,
                    moodColor = getMoodColor(lastMood?.moodRating ?: 0)
                ) {
                    navController.navigate("moodTracker")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Today's Mood Card
                TodaysMoodCard(
                    moodLabel = todayMood?.moodType ?: "No mood logged",
                    moodRating = todayMood?.moodRating ?: 0,
                    moodColor = getMoodColor(todayMood?.moodRating ?: 0)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // quick actions
                Text(
                    text = "Quick Actions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(16.dp))

                QuickActionGrid(navController)

                Spacer(modifier = Modifier.height(24.dp))
            }

            BottomNavBar(navController)
        }
    }
}

// Helper function to map mood rating to color
fun getMoodColor(rating: Int): Color {
    return when {
        rating >= 4 -> Color(0xFF4CAF50)
        rating >= 2 -> Color(0xFFFFC107)
        rating > 0 -> Color(0xFFFF5252)
        else -> Color.Gray
    }
}

// TopBar
@Composable
fun TopBar(username: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = "Hello, $username",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "How are you feeling today?",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        // settings
        IconButton(onClick = { navController.navigate("settings") }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color(0xFF64B5F6)
            )
        }
    }
}

// Last Mood Card
@Composable
fun LastMoodCard(moodLabel: String, moodRating: Int, moodColor: Color, onCheckInClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Last Mood Text
            Text(
                text = "Last Mood: $moodLabel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = moodColor
            )

            // Mood Rating
            if (moodRating > 0) {
                Text(
                    text = "Rating: $moodRating/5",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = moodColor
                )
            }

            Spacer(Modifier.height(16.dp)) //spacing

            Button(
                onClick = onCheckInClick,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF64B5F6))
            ) {
                // Mood Check Button
                Text("Quick Mood Check →", color = Color.White)
            }
        }
    }
}

// Today's Mood Card
@Composable
fun TodaysMoodCard(moodLabel: String, moodRating: Int, moodColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color(0xFFF5F7FE)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Todays mood tex
            Text("Today's Mood", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Text(moodLabel, fontSize = 20.sp, color = moodColor, fontWeight = FontWeight.Bold)

            if (moodRating > 0) {
                // Rating
                Text(
                    text = "Rating: $moodRating/5",
                    fontSize = 18.sp,
                    color = moodColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Quick Actions Grid
@Composable
fun QuickActionGrid(navController: NavController) {
    val actions = listOf(
        "Mood Tracker" to Icons.Default.EmojiEmotions,
        "Habits" to Icons.Default.List,
        "Daily Journal" to Icons.Default.Book,
        "Community" to Icons.Default.Group,
        "Mindfulness" to Icons.Default.Spa,
        "Insights" to Icons.Default.Lightbulb
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        for (row in actions.chunked(2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for ((label, icon) in row) {
                    QuickActionCard(icon, label) {
                        when (label) {
                            "Mood Tracker" -> navController.navigate("moodTracker")
                            "Daily Journal" -> navController.navigate("journalList")
                            "Mindfulness" -> navController.navigate("mindfulness")
                            "Insights" -> navController.navigate("progress")
                        }
                    }
                }
                if (row.size < 2) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
// Quick Action Crads
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = text, tint = Color(0xFF64B5F6), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF333333))
        }
    }
}

// Bottom Navigation
@Composable
fun BottomNavBar(navController: NavController) {
    val navItems = listOf(
        // Home Icon
        Icons.Default.Home to "Home",

        // Mood Icon
        Icons.Default.EmojiEmotions to "Mood",

        // Insights Icon
        Icons.Default.Lightbulb to "Insights",

        // Profile Icon
        Icons.Default.Person to "Profile"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for ((icon, label) in navItems) {
            NavItem(icon, label) {
                when (label) {
                    "Home" -> navController.navigate("home")
                    "Mood" -> navController.navigate("moodTracker")
                    "Insights" -> navController.navigate("progress")
                    "Profile" -> navController.navigate("settings")
                }
            }
        }
    }
}

@Composable
// Nav Item
fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        // Icon
        Icon(icon, contentDescription = label, tint = Color(0xFF8E8E8E))

        // Label Text
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}