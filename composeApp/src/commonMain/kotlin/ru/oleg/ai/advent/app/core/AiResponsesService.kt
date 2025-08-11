package ru.oleg.ai.advent.app.core

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.oleg.ai.advent.app.data.ai.Messages
import ru.oleg.ai.advent.app.data.ai.ResponseOutput
import ru.oleg.ai.advent.app.data.ai.ResponseRequest
import ru.oleg.ai.advent.app.data.ai.day2.CombinedResponse
import ru.oleg.ai.advent.app.data.ai.day2.XmlResponse

class AiResponsesService(
    val http: HttpClient
) {
    private val base = "https://openrouter.ai/api/v1/chat/completions"

    suspend fun getReply(
        model: String,
        userText: String
    ): Result<CombinedResponse> = runCatching {

        val requestTasks = buildList {
            add(Messages(role = "system", content = """
                Отвечай строго в формате XML. Никакого текста вне XML.
                Единственный допустимый корневой элемент: <response>.
                Внутри <response> обязательно и строго в таком порядке укажи три тега:

                <answer> — полный ответ на запрос.

                <points> — содержит объекты point для нумерации шагов размышления/ход решения (не раскрывай приватные цепочки рассуждений; дай краткие, проверяемые шаги высокого уровня).

                <summary> — краткое заключение в 1–2 предложения.

                Правила оформления:

                Используй только эти 4 тега. Никаких дополнительных атрибутов/пространств имён.

                Весь текст заключай в <![CDATA[ ... ]]>, чтобы избежать проблем с символами.

                Не используй Markdown, JSON, комментарии, преамбулы XML и прочие форматы.

                Поддерживай валидный, хорошо сформированный XML (один корень, корректные закрывающие теги).

                Пиши на языке пользователя входного запроса.

                Если нечего ответить, оставь соответствующий CDATA пустым, но теги всё равно выведи.

                Пример структуры, которой нужно строго следовать:
                Пример ответа:
                <response>
                  <answer>…текст развернутого ответа…</answer>
                  <points>
                    <point>Шаг 1></point>
                    <point>Шаг 2></point>
                    <point>Шаг 3></point>
                  ></points>
                  <summary>…краткое резюме на 1–2 предложения…></summary>
                </response>
            """.trimIndent()))
            add(Messages(content = userText))
        }

        val req = ResponseRequest(model = model, requestTasks)
        val resp: ResponseOutput = http.post(base) {
            setBody(req)
        }.body()

        val outputXml = resp.choices?.firstOrNull()?.message?.content
            ?: ""

        val responseRegex = Regex("(?s)<response>.*?</response>")

        fun extractResponseXml(input: String): String =
            responseRegex.find(input)?.value
                ?: error("response tag not found")

        val responseXml = extractResponseXml(outputXml)

        val xmlResponse = ChatXmlParser.xml.decodeFromString<XmlResponse>(responseXml, null)

        CombinedResponse(responseXml, xmlResponse)
    }
}
