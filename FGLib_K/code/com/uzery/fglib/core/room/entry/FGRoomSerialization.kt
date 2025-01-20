package com.uzery.fglib.core.room.entry

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

abstract class FGRoomSerialization {
    abstract fun stringFrom(room: FGRoomEntry): String
    abstract fun roomFrom(source: String): FGRoomEntry

    open fun decoratorStringFrom(room: FGRoomEntry, getter: AbstractClassGetter<GameObject>): String {
        return stringFrom(room)
    }
}
