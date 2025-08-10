package ru.oleg.ai.advent.app.data.ai

@kotlinx.serialization.Serializable
data class ResponseRequest(
    val model: String,
    val messages: List<Messages>,
)

@kotlinx.serialization.Serializable
data class Messages(
    val role: String = "user",
    val content: String,
)
