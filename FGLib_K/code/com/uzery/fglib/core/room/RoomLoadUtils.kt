package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.entry.FGRoomFormat
import com.uzery.fglib.core.room.entry.FGRoomLoadEntry
import com.uzery.fglib.core.room.entry.FGRoomSerialization
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

/**
 * TODO("doc")
 **/
object RoomLoadUtils {
    fun makeRoom(
        getter: AbstractClassGetter<GameObject>,
        entry: FGRoomLoadEntry
    ): Room {
        val objects = ArrayList<GameObject>()
        entry.room.objs.forEach { e -> objects.add(getter[e]) }
        val room = Room(entry.room.pos, entry.room.size, objects)
        entry.masks.forEach { mask -> mask.apply(room, getter) }
        return room
    }

    fun readRoom(
        getter: AbstractClassGetter<GameObject>,
        filename: String,
        serialization: FGRoomSerialization = FGRoomFormat
    ): Room {
        val entry = serialization.readFrom(filename)
        return makeRoom(getter, entry)
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
