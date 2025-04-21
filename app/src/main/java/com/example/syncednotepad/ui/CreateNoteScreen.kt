package com.example.syncednotepad.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(navController: NavController, noteId: String?) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var isLoaded by remember { mutableStateOf(false) }

    val notesRef = FirebaseDatabase.getInstance()
        .getReference("users/${FirebaseAuth.getInstance().currentUser?.uid}/notes")

    LaunchedEffect(noteId) {
        if (noteId != null && !isLoaded) {
            notesRef.child(noteId).get().addOnSuccessListener { snapshot ->
                val note = snapshot.getValue(com.example.syncednotepad.model.Note::class.java)
                note?.let {
                    title = it.title
                    content = it.content
                    tags = it.tags.joinToString(", ")
                    isLoaded = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId == null) "Create Note" else "Edit Note") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it.replace(Regex("[\\n\\r]"), "") },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        val tagsList = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        val note = mapOf(
                            "title" to title,
                            "content" to content,
                            "tags" to tagsList,
                            "updatedAt" to System.currentTimeMillis()
                        )
                        if (noteId == null) notesRef.push().setValue(note)
                        else notesRef.child(noteId).setValue(note)
                        navController.popBackStack()
                    }
                }, modifier = Modifier.weight(1f)) {
                    Text(if (noteId == null) "Save Note" else "Update Note")
                }

                OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
            }
        }
    }
}