package com.uzery.fglib.utils.input.data

import com.uzery.fglib.core.program.Platform.packager

data class FGMouseKey(val value: String, val id: Int) {
    constructor(value: String): this(value, from(value).id)
    companion object {
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
    }
}
