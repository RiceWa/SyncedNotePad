package com.example.syncednotepad.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.syncednotepad.model.Note
import com.example.syncednotepad.ui.components.DeleteDialog
import com.example.syncednotepad.ui.components.NoteCard
import com.example.syncednotepad.ui.components.SearchBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Composable
fun NotesScreen(navController: NavController) {
    val notesRef = FirebaseDatabase.getInstance()
        .getReference("users/${FirebaseAuth.getInstance().currentUser?.uid}/notes")

    var notes by remember { mutableStateOf(listOf<Note>()) }
    var searchQuery by remember { mutableStateOf("") }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    // Load notes in real-time
    LaunchedEffect(Unit) {
        notesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newNotes = mutableListOf<Note>()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    note?.let { newNotes.add(it.copy(id = noteSnapshot.key ?: "")) }
                }
                notes = newNotes.sortedByDescending { it.updatedAt }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Note")
            }

        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Your Notes", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            SearchBar(searchQuery) { searchQuery = it }

            Spacer(modifier = Modifier.height(16.dp))

            val filteredNotes = notes.filter {
                val query = searchQuery.lowercase()
                it.title.lowercase().contains(query) ||
                        it.content.lowercase().contains(query) ||
                        it.tags.any { tag -> tag.lowercase().contains(query) }
            }

            LazyColumn(Modifier.weight(1f)) {
                items(filteredNotes) { note ->
                    NoteCard(note,
                        onClick = { navController.navigate("edit/${note.id}") },
                        onDelete = { noteToDelete = note }
                    )
                }
            }

            noteToDelete?.let { note ->
                DeleteDialog(
                    note = note,
                    onDelete = {
                        notesRef.child(note.id).removeValue()
                        noteToDelete = null
                    },
                    onCancel = { noteToDelete = null }
                )
            }
        }
    }
}