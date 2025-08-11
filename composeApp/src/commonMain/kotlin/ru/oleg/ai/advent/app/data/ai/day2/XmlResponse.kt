package ru.oleg.ai.advent.app.data.ai.day2

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("response", "", "")
data class XmlResponse(
    @SerialName("answer")  @XmlElement(true) val answer: String = "",
    @SerialName("points")  @XmlElement(true) val points: Points = Points(),
    @SerialName("summary") @XmlElement(true) val summary: String = ""
)

@Serializable
@XmlSerialName("points", "", "")
data class Points(
    @SerialName("point") @XmlElement(true) val point: List<String> = emptyList()
)
