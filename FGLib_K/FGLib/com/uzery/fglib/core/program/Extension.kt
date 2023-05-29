package com.uzery.fglib.core.program

import java.util.*

interface Extension {
    fun update()
    fun init()
    fun children(): List<Extension> = LinkedList<Extension>()
}
