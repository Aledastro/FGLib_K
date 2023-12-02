package com.uzery.fglib.utils.input.data

import com.uzery.fglib.core.program.Platform.packager

data class FGMouseKey(val value: String, val id: Int) {
    companion object {
        val values = arrayOf("PRIMARY", "SECONDARY", "MIDDLE", "BACK", "FORWARD", "NONE")
        val key_values = Array(values.size) { from(values[it]) }

        val PRIMARY = from("PRIMARY")
        val SECONDARY = from("SECONDARY")
        val MIDDLE = from("MIDDLE")
        val BACK = from("BACK")
        val FORWARD = from("FORWARD")
        val NONE = from("NONE")

        fun from(key: String): FGMouseKey {
            return packager.fromMouseKey(key)
        }

        val LEFT
            get() = PRIMARY
        val RIGHT
            get() = SECONDARY

        fun values(): Array<FGMouseKey> {
            return key_values
        }
    }
}
