package com.uzery.fglib.utils.data

interface DebugData {
    companion object {
        fun error(s: String): Throwable {
            return IllegalArgumentException(s)
        }
    }
}