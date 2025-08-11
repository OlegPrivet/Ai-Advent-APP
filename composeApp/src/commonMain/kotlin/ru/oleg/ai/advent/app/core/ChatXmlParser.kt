package ru.oleg.ai.advent.app.core

import nl.adaptivity.xmlutil.serialization.XML

object ChatXmlParser {
    val xml = XML {
        indent = 4
        autoPolymorphic = false
    }
}
