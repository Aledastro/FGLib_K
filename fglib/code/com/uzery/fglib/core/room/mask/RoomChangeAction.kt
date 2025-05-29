package com.uzery.fglib.core.room.mask

import com.uzery.fglib.utils.data.entry.FGEntry

/**
 * TODO("doc")
 **/
sealed class RoomChangeAction(val sign: String, val obj: FGEntry) {
    override fun toString(): String {
        return "$sign $obj"
    }

    operator fun unaryMinus(): RoomChangeAction {
        return when (this) {
            is ADD -> REMOVE(obj)
            is REMOVE -> ADD(obj)
        }
    }

    class ADD(obj: FGEntry): RoomChangeAction("+", obj)
    class REMOVE(obj: FGEntry): RoomChangeAction("-", obj)
}
