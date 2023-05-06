package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import com.uzery.fglib.utils.math.getter.ClassGetterInstance
import javafx.scene.paint.Color
import java.util.*
import java.util.stream.Stream

interface World {
    companion object {
        var active_room = Room(PointN.ZERO, PointN.ZERO)
            private set
        val rooms = LinkedList<Room>()
        private val filenames = LinkedList<String>()

        var controller: WorldController? = null

        fun allTagged(tag: String): Stream<GameObject> = active_room.objects.stream().filter { o -> o.tagged(tag) }
        fun allExists(vararg tag: String) = tag.all { t -> allTagged(t).count() != 0L }
        fun anyExists(vararg tag: String) = tag.any { t -> allTagged(t).count() != 0L }

        fun noneExists(vararg tag: String) = tag.all { t -> allTagged(t).count() == 0L }

        var camera: Camera? = null

        fun next() {
            active_room.next()

            camera?.update()
            graphics.drawPOS = camera?.drawPOS() ?: PointN.ZERO

            if(controller != null && controller!!.ready()) {
                controller?.action()
                //if(controller!!.ready()) throw DebugData.error("")
            }
        }

        fun draw(pos: PointN = PointN.ZERO) {
            active_room.draw(active_room.pos + pos)

            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.stroke.draw(pos, active_room.main, Color.DARKBLUE)
        }

        var getter: ClassGetter<GameObject>? = null

        private fun readInfo(filename: String): Room {
            if(getter == null) throw DebugData.error("getter not loaded")

            val list = WriteData[filename]
            val objects = LinkedList<GameObject>()
            var next = ""

            while(next.startsWith("//") || next.isEmpty()) {
                next = list.removeFirst()
            }
            val t = StringTokenizer(next)
            t.nextToken()

            val pos = getP(t.nextToken() + t.nextToken())
            val size = getP(t.nextToken() + t.nextToken())
            while(list.isNotEmpty()) {
                next = list.removeFirst()
                if(next.startsWith("//")) continue
                if(next.isNotEmpty()) objects.add(getter!!.getFrom(next))
            }

            return Room(pos, size, objects)
        }

        private fun getP(s: String): PointN {
            val c = ClassGetter(object: ClassGetterInstance<PointN>() {
                override fun addAll() = add("pos", 1) { pos }
            })
            return c.getFrom("pos: $s")
        }

        fun init(controller: WorldController?, vararg filename: String) {
            World.controller = controller
            rooms.clear()
            for(i in filename.indices) filenames.add(filename[i])
            filenames.forEach { name -> rooms.add(readInfo(name)) }
            set(0)
        }

        fun init(vararg filename: String) = init(null, *filename)

        fun set(id: Int) {
            active_room = rooms[id]
        }

        fun respawn(id: Int) {
            rooms[id] = readInfo(filenames[id])
            active_room = rooms[id]
        }

        fun add(o: GameObject) = active_room.add(o)
    }
}