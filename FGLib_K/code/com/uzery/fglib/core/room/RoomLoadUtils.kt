package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.entry.FGRoomEntry
import com.uzery.fglib.core.room.entry.FGRoomFormat
import com.uzery.fglib.core.room.entry.FGRoomLoadEntry
import com.uzery.fglib.core.room.entry.RoomSerialization
import com.uzery.fglib.core.room.mask.RoomMask
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
        val room = Room(entry.room.name, entry.room.pos, entry.room.size, objects)
        entry.masks.forEach { mask -> mask.apply(room, getter) }
        return room
    }

    fun makeRoom(
        getter: AbstractClassGetter<GameObject>,
        room: FGRoomEntry,
        masks: ArrayList<RoomMask> = ArrayList()
    ): Room {
        return makeRoom(getter, FGRoomLoadEntry(room, masks))
    }

    fun readRoom(
        getter: AbstractClassGetter<GameObject>,
        filename: String,
        serialization: RoomSerialization = FGRoomFormat
    ): Room {
        val entry = serialization.readFrom(filename)
        return makeRoom(getter, entry)
    }

    fun readRooms(
        getter: AbstractClassGetter<GameObject>,
        filenames: Array<String>,
        serialization: RoomSerialization = FGRoomFormat
    ): Array<Room> {
        return Array(filenames.size) { i ->
            readRoom(getter, filenames[i], serialization)
        }
    }

    fun readAllFrom(
        getter: AbstractClassGetter<GameObject>,
        dir: String,
        serialization: RoomSerialization = FGRoomFormat
    ): Array<Room> {
        return readRooms(getter, TextData.filesFrom(dir).toTypedArray(), serialization)
    }
}
