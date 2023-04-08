package com.uzery.fglib.utils.data

import java.lang.IllegalArgumentException

interface DebugData {
    companion object{
        fun error(s: String): Throwable{
            return IllegalArgumentException(s)
        }
    }
}