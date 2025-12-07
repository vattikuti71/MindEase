package com.example.mindease.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mindease.data.JournalEntry
import com.example.mindease.viewmodel.JournalViewModel
import com.example.mindease.viewmodel.JournalViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(navController: NavController) {

    // get app
    val application = LocalContext.current.applicationContext as Application
    // init VM
    val vm: JournalViewModel = viewModel(factory = JournalViewModelFactory(application))
    // observe data
    val entries by vm.allEntries.collectAsState()

    Scaffold(
        containerColor = Color.White,
        // TopBar
        topBar = {
            TopAppBar(
                title = { Text("Journal", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                ),
                navigationIcon = {
                    // back nav
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            Box(modifier = Modifier.padding(bottom = 20.dp)) {
                // FAB padding
                FloatingActionButton(
                    // navigate add
                    onClick = { navController.navigate("journalCreate") },
                    containerColor = Color(0xFF0D47A1),
                    contentColor = Color.White
                ) {
                    // add icon
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .background(Color.White)
        ) {
            items(entries) { entry ->
                JournalListItem(entry) {
                    navController.navigate("journalDetail/${entry.id}")
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun JournalListItem(entry: JournalEntry, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // title
            Text(
                text = entry.title.ifBlank { "Untitled" },
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp)) // spacing

            Text(
                text = entry.content.take(140) +
                        if (entry.content.length > 140) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(6.dp)) // spacing

            // format date
            val formattedDate = remember(entry.updatedAt) {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(Date(entry.updatedAt))
            }

            // date text
            Text(
                text = "Updated: $formattedDate",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF0D47A1)
            )
        }
    }
}