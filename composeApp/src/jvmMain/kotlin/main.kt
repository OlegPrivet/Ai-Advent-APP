import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import java.awt.Dimension
import ru.oleg.ai.advent.app.core.Http
import ru.oleg.ai.advent.app.core.AiResponsesService
import ru.oleg.ai.advent.app.features.chat.ChatScreen
import ru.oleg.ai.advent.app.features.chat.vm.ChatViewModel
import ru.oleg.ai.advent.app.theme.AppTheme

fun main() = application {
    Logger.setLogWriters(platformLogWriter())
    Logger.setTag("DesktopApp")
    Logger.i { "Приложение стартует" }
    val apiKey = "api"
    val client = Http.client(apiKey)
    val service = AiResponsesService(client)
    val vm = remember { ChatViewModel(service, model = "qwen/qwq-32b:free") } // пример
    Window(
        title = "Ai Advent APP",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        AppTheme {
            ChatScreen(vm)
        }
    }
}

