package ru.oleg.ai.advent.app.data.chat

data class ChatMessage(
    val id: String,
    val text: String,
    val isGPT: Boolean,
)
