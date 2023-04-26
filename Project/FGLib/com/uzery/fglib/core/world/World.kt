package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import com.uzery.fglib.utils.math.getter.ClassGetterInstance
import java.util.*

class World(instance: ClassGetterInstance<GameObject>) {
    private var room = Room(PointN.ZERO, PointN.ZERO)

    private var getter = ClassGetter(instance)

    fun setRoom(filename: String) {
        readInfo(WriteData[filename])
    }

    private fun readInfo(list: ArrayList<String>) {
        val objects = LinkedList<GameObject>()
        var next = ""
        while(next.startsWith("//") || next.isEmpty()) {
            next = list.removeFirst()
        }
        val pos = getP(next.substring(next.indexOf(':') + 2))
        next = list.removeFirst()
        val size = getP(next.substring(next.indexOf(':') + 2))
        while(list.isNotEmpty()) {
            next = list.removeFirst()
            if(next.startsWith("//")) continue
            if(next.isNotEmpty()) objects.add(getter.getFrom(next))
        }
        room = Room(pos, size, objects)
    }

    private fun getP(s: String): PointN {
        val c = ClassGetter(object: ClassGetterInstance<PointN>() {
            override fun addAll() = add("r", 1) { pos }
        })
        return c.getFrom("r: $s")
    }


    fun init(s: String) {
        setRoom(s)
    }

    fun run() {
        room.next()
        room.draw(room.pos)
    }

    fun add(o: GameObject) {
        room.add(o)
    }

    fun r(): Room {
        return room
    }
}