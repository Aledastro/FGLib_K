package com.uzery.fglib.core.room.entry

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.RoomLoadUtils
import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.entry.FGFormat
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.getter.AbstractClassGetter
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.geom.shape.RectN
import java.util.*

object FGRoomFormat: FGRoomSerialization() {
    override fun writeTo(filepath: String, entry: FGRoomLoadEntry, getter: AbstractClassGetter<GameObject>) {
        val room = RoomLoadUtils.makeRoom(getter, entry).toString()

        TextData.write(filepath, room, true)
    }

    override fun readFrom(filepath: String): FGRoomLoadEntry {
        val room = getRoomEntry(filepath)

        return FGRoomLoadEntry(room, ArrayList())
    }

    /////////////////////////////////////////////////////////////////////////////////////

    private val room_info_cg = object: ClassGetter<RectN>() {
        override fun addAll() {
            add("room", 2) { RectN(pos, size) }
        }
    }
    private fun getRoomEntry(filename: String): FGRoomEntry {
        val lines = TextData.readLines(filename)

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
