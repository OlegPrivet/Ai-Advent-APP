package ru.oleg.ai.advent.app.core

import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Http {
    fun client(apiKey: String) = io.ktor.client.HttpClient {
        expectSuccess = false
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            }.let { json -> json(json) }
        }
        install(io.ktor.client.plugins.logging.Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.i { "[HTTP] $message" }
                }
            }
            level = io.ktor.client.plugins.logging.LogLevel.BODY
        }
        defaultRequest {
            header("Authorization", "Bearer $apiKey")   // Bearer-ключ
//            header("OpenAI-Beta", "assistants=v2")      // если потребуется для некоторых фич
            header("Content-Type", "application/json")
        }
        // таймауты/ретраи по желанию
    }
}
