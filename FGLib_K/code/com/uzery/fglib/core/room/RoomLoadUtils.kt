package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.getter.AbstractClassGetter
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.geom.PointN
import java.util.*

/**
 * TODO("doc")
 **/
object RoomLoadUtils {
    val room_info_cg = object: ClassGetter<Pair<PointN, PointN>>() {
        override fun addAll() {
            add("room", 2) { Pair(pos, size) }
        }
    }

    fun makeRoom(getter: AbstractClassGetter<GameObject>, input: List<String>): Room {
        val list = LinkedList<String>()
        list.addAll(input)

        list.removeIf { FGUtils.isComment(it) }

        val objects = ArrayList<GameObject>()
        var next = ""

        next = list.removeFirst()

        val room_info = room_info_cg[next]

        while (list.isNotEmpty()) {
            next = list.removeFirst()
            if (FGUtils.isComment(next)) continue
            objects.add(getter[next])
        }

        return Room(room_info.first, room_info.second, objects)
    }

    fun readRoom(getter: AbstractClassGetter<GameObject>, filename: String): Room {
        return makeRoom(getter, TextData[filename])
    }

    fun readRooms(getter: AbstractClassGetter<GameObject>, filenames: Array<String>): Array<Room> {
        return Array(filenames.size) { i ->
            readRoom(getter, filenames[i])
        }
    }

    fun readAllFrom(getter: AbstractClassGetter<GameObject>, dir: String): Array<Room> {
        return readRooms(getter, TextData.filesFrom(dir).toTypedArray())
    }
}
