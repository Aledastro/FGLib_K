package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.entry.FGRoomEntry
import com.uzery.fglib.core.room.entry.FGRoomFormat
import com.uzery.fglib.core.room.entry.FGRoomSerialization
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

/**
 * TODO("doc")
 **/
object RoomLoadUtils {
    fun makeRoom(entry: FGRoomEntry, getter: AbstractClassGetter<GameObject>): Room {
        val objects = ArrayList<GameObject>()
        entry.objs.forEach { e -> objects.add(getter[e]) }

        return Room(entry.pos, entry.size, objects)
    }

    fun readRoom(
        getter: AbstractClassGetter<GameObject>,
        filename: String,
        serialization: FGRoomSerialization = FGRoomFormat
    ): Room {
        val entry = serialization.roomFrom(TextData.read(filename))
        return makeRoom(entry, getter)
    }

    fun readRooms(
        getter: AbstractClassGetter<GameObject>,
        filenames: Array<String>,
        serialization: FGRoomSerialization = FGRoomFormat
    ): Array<Room> {
        return Array(filenames.size) { i ->
            readRoom(getter, filenames[i], serialization)
        }
    }

    fun readAllFrom(
        getter: AbstractClassGetter<GameObject>,
        dir: String,
        serialization: FGRoomSerialization = FGRoomFormat
    ): Array<Room> {
        return readRooms(getter, TextData.filesFrom(dir).toTypedArray(), serialization)
    }
}
