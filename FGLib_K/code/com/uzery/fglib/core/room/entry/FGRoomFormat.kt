package com.uzery.fglib.core.room.entry

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.RoomLoadUtils
import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.entry.FGFormat
import com.uzery.fglib.utils.data.getter.AbstractClassGetter
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.geom.shape.RectN
import java.util.*

object FGRoomFormat: FGRoomSerialization() {
    val room_info_cg = object: ClassGetter<RectN>() {
        override fun addAll() {
            add("room", 2) { RectN(pos, size) }
        }
    }

    override fun decoratorStringFrom(room: FGRoomEntry, getter: AbstractClassGetter<GameObject>): String {
        return RoomLoadUtils.makeRoom(room, getter).toString()
    }

    override fun stringFrom(room: FGRoomEntry): String {
        return room.toString().replaceFirst("room entry", "room")
    }

    override fun roomFrom(source: String): FGRoomEntry {
        val lines = source.split("\n")

        val list = LinkedList<String>()
        list.addAll(lines)

        list.removeIf { FGUtils.isComment(it) }

        val objects = ArrayList<FGEntry>()
        var next = list.removeFirst()

        val room_info = room_info_cg[next]

        while (list.isNotEmpty()) {
            next = list.removeFirst()
            if (FGUtils.isComment(next)) continue
            objects.add(FGFormat.entryFrom(next))
        }

        return FGRoomEntry(room_info.pos, room_info.size, objects)
    }
}
