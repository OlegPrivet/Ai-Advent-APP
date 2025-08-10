package ru.oleg.ai.advent.app.core

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.oleg.ai.advent.app.data.ai.Messages
import ru.oleg.ai.advent.app.data.ai.ResponseOutput
import ru.oleg.ai.advent.app.data.ai.ResponseRequest

class AiResponsesService(
    val http: HttpClient
) {
    private val base = "https://openrouter.ai/api/v1/chat/completions"

    suspend fun getReply(
        model: String,
        userText: String
    ): Result<String> = runCatching {
        val req = ResponseRequest(model = model, listOf(Messages(content = userText)))
        val resp: ResponseOutput = http.post(base) {
            setBody(req)
        }.body()

        resp.choices?.firstOrNull()?.message?.content
            ?: ""
    }
}
