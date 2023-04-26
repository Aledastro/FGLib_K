package com.uzery.fglib.core.program

interface Extension {
    fun update()
    fun init()
    fun children(): List<Extension>
}
