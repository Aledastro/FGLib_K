package com.uzery.fglib.core.program

import java.util.*

interface Extension {
    fun update()
    fun init()

    fun isRunning() = true
    fun children(): List<Extension> = LinkedList<Extension>()
}
