package ru.oleg.ai.advent.app.data.ai

@kotlinx.serialization.Serializable
data class OutputItem(
    val message: Message,
)

@kotlinx.serialization.Serializable
data class Message(
    val content: String? = null
)


