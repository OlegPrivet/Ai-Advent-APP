package ru.oleg.ai.advent.app.features.chat.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.oleg.ai.advent.app.core.AiResponsesService
import ru.oleg.ai.advent.app.data.chat.ChatMessage
import ru.oleg.ai.advent.app.data.chat.state.ChatState
import java.util.UUID

class ChatViewModel(
    private val openAi: AiResponsesService,
    private val model: String
): ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    fun send(userText: String) {
        val uid = UUID.randomUUID().toString()
        val userMsg = ChatMessage(uid, text = userText, isGPT = false)
        _state.update { it.copy(messages = it.messages + userMsg, isSending = true, error = null) }

        viewModelScope.launch {
            openAi.getReply(model, userText)
                .onSuccess { combinedResponse ->
                    _state.update { it.copy(
                        messages = it.messages + ChatMessage(
                            UUID.randomUUID().toString(),
                            xmlResponse = combinedResponse.xmlResponse,
                            rawText = combinedResponse.raw,
                            isGPT = true,
                            text = ""
                        ),
                        isSending = false
                    ) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isSending = false, error = e.message) }
                }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            _state.update { it.copy(messages = emptyList()) }
        }
    }
}
