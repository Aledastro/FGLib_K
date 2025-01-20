package com.uzery.fglib.core.room.entry

abstract class FGRoomSerialization {
    abstract fun stringFrom(room: FGRoomEntry): String
    abstract fun roomFrom(source: String): FGRoomEntry
}
