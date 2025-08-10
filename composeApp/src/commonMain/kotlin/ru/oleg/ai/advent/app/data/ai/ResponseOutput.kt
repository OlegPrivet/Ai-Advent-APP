package ru.oleg.ai.advent.app.data.ai

@kotlinx.serialization.Serializable
data class ResponseOutput(
    val id: String? = null,
    val choices: List<OutputItem>? = null
)
