package com.uzery.fglib.core.room.entry

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

abstract class RoomSerialization {
    abstract fun writeTo(filepath: String, entry: FGRoomLoadEntry, getter: AbstractClassGetter<GameObject>)
    abstract fun readFrom(filepath: String): FGRoomLoadEntry
}
