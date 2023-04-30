package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import com.uzery.fglib.utils.math.getter.ClassGetterInstance
import javafx.scene.paint.Color
import java.util.*
import java.util.stream.Stream

class World(private val getter: ClassGetter<GameObject>) {
    companion object {
        var room = Room(PointN.ZERO, PointN.ZERO)
            private set

        fun allTagged(tag: String): Stream<GameObject> {
            return room.objects.stream().filter { o -> o.tagged(tag) }
        }

        var camera: Camera? = null
    }

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


    fun init(filename: String) {
        setRoom(filename)
    }

    fun run() {
        room.next()

        room.draw(room.pos)
        camera?.update()
        Platform.graphics.drawPOS = camera?.drawPOS() ?: PointN.ZERO

        Platform.graphics.layer = DrawLayer.CAMERA_FOLLOW
        Platform.graphics.stroke.rect(room.pos, room.size, Color.DARKBLUE)
    }

    fun add(o: GameObject) {
        room.add(o)
    }
}