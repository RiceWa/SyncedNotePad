package com.example.syncednotepad

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.syncednotepad.ui.CreateNoteScreen
import com.example.syncednotepad.ui.NotesScreen
import com.example.syncednotepad.ui.theme.SyncedNotePadTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sign in anonymously to Firebase
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("Firebase", "Auth failed: ${task.exception}")
                }
            }

        // Set up Compose UI and navigation
        setContent {
            SyncedNotePadTheme(dynamicColor = false) {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { NotesScreen(navController) }
                    composable("create") { CreateNoteScreen(navController, null) }
                    composable("edit/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")
                        CreateNoteScreen(navController, noteId)
                    }
                }
            }
        }
    }
}