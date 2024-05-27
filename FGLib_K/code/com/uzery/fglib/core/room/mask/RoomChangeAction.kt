package com.uzery.fglib.core.room.mask

import com.uzery.fglib.utils.data.debug.DebugData

class RoomChangeAction(val sign: String, val obj: String) {
    constructor(action: String): this(
        action.substringBefore(' '),
        action.substringAfter(' ')
    )

    override fun toString(): String {
        return "$sign $obj"
    }

    operator fun unaryMinus(): RoomChangeAction {
        val new_sign = when (sign) {
            "+" -> "-"
            "-" -> "+"
            else -> throw DebugData.error("Unsupported sign: $sign")
        }
        return RoomChangeAction(new_sign, obj)
    }
}
