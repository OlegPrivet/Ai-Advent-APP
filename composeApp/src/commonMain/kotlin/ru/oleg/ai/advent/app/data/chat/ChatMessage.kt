package ru.oleg.ai.advent.app.data.chat

import ru.oleg.ai.advent.app.data.ai.day2.XmlResponse

data class ChatMessage(
    val id: String,
    val text: String,
    val xmlResponse: XmlResponse? = null,
    val rawText: String? = null,
    val isGPT: Boolean,
)
