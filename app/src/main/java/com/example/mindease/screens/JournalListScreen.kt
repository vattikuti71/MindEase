package com.example.mindease.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mindease.data.JournalEntry
import com.example.mindease.viewmodel.JournalViewModel
import com.example.mindease.viewmodel.JournalViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as Application
    val vm: JournalViewModel = viewModel(factory = JournalViewModelFactory(application))
    val entries by vm.allEntries.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Journal") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("journalCreate") }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
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
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.title.ifBlank { "Untitled" }, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = entry.content.take(120) + if (entry.content.length > 120) "..." else "", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Updated: ${java.text.SimpleDateFormat("dd MMM yyyy").format(java.util.Date(entry.updatedAt))}", style = MaterialTheme.typography.bodySmall)
        }
    }
}