package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.math.getter.ClassGetter
import com.uzery.fglib.utils.math.getter.ClassGetterInstance
import java.util.*

class World(instance: ClassGetterInstance<GameObject>) {
    private var room = Room(0, 0)

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
        val width = next.substring(next.indexOf(':') + 2).toInt()
        next = list.removeFirst()
        val height = next.substring(next.indexOf(':') + 2).toInt()
        while(list.isNotEmpty()) {
            next = list.removeFirst()
            if(next.startsWith("//")) continue
            if(next.isNotEmpty()) objects.add(getter.getFrom(next))
        }
        room = Room(width, height, objects)
    }


    fun init(s: String) {
        setRoom(s)
    }

    fun run() {
        room.next()
        room.draw()
    }

    fun add(o: GameObject) {
        room.add(o)
    }

    fun r(): Room {
        return room
    }
}