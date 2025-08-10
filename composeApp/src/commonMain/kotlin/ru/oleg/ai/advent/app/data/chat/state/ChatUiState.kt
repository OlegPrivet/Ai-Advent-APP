package ru.oleg.ai.advent.app.data.chat.state

import ru.oleg.ai.advent.app.data.chat.ChatMessage

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val error: String? = null
)
