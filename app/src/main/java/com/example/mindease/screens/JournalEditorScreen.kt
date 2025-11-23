package com.example.mindease.screens

import android.app.Application
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEditorScreen(navController: NavController, entryId: Int? = null) {
    val application = LocalContext.current.applicationContext as Application
    val vm: JournalViewModel = viewModel(factory = JournalViewModelFactory(application))
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var loadedEntry by remember { mutableStateOf<JournalEntry?>(null) }

    LaunchedEffect(entryId) {
        if (entryId != null) {
            val e = vm.getEntryById(entryId)
            e?.let {
                loadedEntry = it
                title = it.title
                content = it.content
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (entryId == null) "New Entry" else "Edit Entry") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Write your thoughts...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                maxLines = Int.MAX_VALUE
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    coroutineScope.launch {
                        if (loadedEntry == null) {
                            vm.addEntry(title, content)
                        } else {
                            vm.updateEntry(loadedEntry!!.copy(title = title, content = content))
                        }
                        navController.navigate("journalList") {
                            popUpTo("journalList") { inclusive = true }
                        }
                    }
                }, modifier = Modifier.weight(1f)) {
                    Text("Save")
                }

                if (loadedEntry != null) {
                    Button(colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Red),
                        onClick = {
                            coroutineScope.launch {
                                loadedEntry?.let {
                                    vm.deleteEntry(it)
                                    navController.navigate("journalList") {
                                        popUpTo("journalList") { inclusive = true }
                                    }
                                }
                            }
                        }, modifier = Modifier.weight(1f)) {
                        Text("Delete", color = androidx.compose.ui.graphics.Color.White)
                    }
                }
            }
        }
    }
}