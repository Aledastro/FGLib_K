package com.uzery.fglib.utils.data.debug

interface DebugData {
    companion object {
        fun error(s: String): Throwable {
            return IllegalArgumentException(s)
        }
    }
}