package com.example.syncednotepad.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.syncednotepad.model.Note

@Composable
fun DeleteDialog(note: Note, onDelete: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Delete Note?") },
        text = { Text("Are you sure you want to delete \"${note.title}\"?") },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}
