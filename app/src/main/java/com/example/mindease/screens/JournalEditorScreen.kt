package com.example.mindease.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mindease.data.JournalEntry
import com.example.mindease.notifications.NotificationUtils
import com.example.mindease.viewmodel.JournalViewModel
import com.example.mindease.viewmodel.JournalViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEditorScreen(navController: NavController, entryId: Int? = null) {

    // get context
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val vm: JournalViewModel = viewModel(factory = JournalViewModelFactory(application))
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    // loaded entry
    var loadedEntry by remember { mutableStateOf<JournalEntry?>(null) }

    // load entry
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

    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            // Top Bar
            TopAppBar(
                title = { Text(if (entryId == null) "New Journal Entry" else "Edit Entry", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3)),
                navigationIcon = {
                    // back button
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Write your thoughts...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp),
                maxLines = 10,
                textStyle = TextStyle(fontSize = 16.sp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (loadedEntry == null) {
                                vm.addEntry(title, content)
                                NotificationUtils.sendNotification(
                                    context,
                                    "Journal Added",
                                    "New journal entry saved successfully"
                                )
                            } else {
                                vm.updateEntry(loadedEntry!!.copy(title = title, content = content))
                                NotificationUtils.sendNotification(
                                    context,
                                    "Journal Updated",
                                    "Your journal entry was updated"
                                )
                            }
                            navController.navigate("journalList") {
                                popUpTo("journalList") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Text("Save", color = Color.White)
                }

                if (loadedEntry != null) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                loadedEntry?.let { entry ->
                                    vm.deleteEntry(entry)       // delete
                                    NotificationUtils.sendNotification(
                                        context,
                                        "Journal Deleted",
                                        "A journal entry was deleted"
                                    )
                                    // go list
                                    navController.navigate("journalList") {
                                        popUpTo("journalList") { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                }
            }
        }
    }
}