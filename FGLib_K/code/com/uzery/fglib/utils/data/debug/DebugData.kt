package com.uzery.fglib.utils.data.debug

/**
 * TODO("doc")
 **/
object DebugData {
    fun error(s: String): Throwable {
        return IllegalArgumentException(s)
    }
}
