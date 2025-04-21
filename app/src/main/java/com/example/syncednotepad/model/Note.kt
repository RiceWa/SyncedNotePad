package com.example.syncednotepad.model

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    val updatedAt: Long = 0L
)