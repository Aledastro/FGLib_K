package com.uzery.fglib.utils.data.debug

object DebugData {
    fun error(s: String): Throwable {
        return IllegalArgumentException(s)
    }
}
