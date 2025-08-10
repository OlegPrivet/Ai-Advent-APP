package ru.oleg.ai.advent.app.features.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.oleg.ai.advent.app.data.chat.ChatMessage
import ru.oleg.ai.advent.app.features.chat.vm.ChatViewModel

@Composable
fun ChatScreen(vm: ChatViewModel) {
    val state by vm.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LoadingView(state.isSending)
        ErrorView(state.error)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            MessagesList(state.messages, Modifier.weight(1f))
            ButtonSend(state.isSending, send = vm::send, Modifier.fillMaxWidth())
        }

    }
}

@Composable
fun MessagesList(messages: List<ChatMessage>, modifier: Modifier = Modifier) {
    LazyColumn(modifier, reverseLayout = true) {
        items(items = messages, key = { it.id }) { msg ->
            MessageBubble(msg)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ButtonSend(isSending: Boolean, send: (String) -> Unit, modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    val buttonState by remember {
        derivedStateOf {
            !isSending && input.isNotBlank()
        }
    }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Введите сообщение…") }
        )
        Spacer(Modifier.width(8.dp))
        Button(
            enabled = buttonState,
            onClick = {
                send(input.trim())
                input = ""
            }
        ) { Text("Отправить") }
    }
}

@Composable
fun MessageBubble(msg: ChatMessage) {
    val bg = if (msg.isGPT) Color(0xffff4e00) else Color(0xFF3A3D5C)
    val align = if (msg.isGPT) Alignment.CenterStart else Alignment.CenterEnd
    Box(Modifier.fillMaxWidth(), contentAlignment = align) {
        Surface(
            color = bg,
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 2.dp
        ) {
            Text(
                text = msg.text,
                modifier = Modifier.padding(12.dp),
                color = Color.White
            )
        }
    }
}

@Composable
fun LoadingView(isSending: Boolean) {
    if (isSending) {
        Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(100.dp))
        }
    }
}

@Composable
fun ErrorView(error: String?) {
    error?.let {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = error)
        }
    }
}
