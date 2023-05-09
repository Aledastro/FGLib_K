package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.math.FGUtils
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
        var active_id = -1
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

            controller?.update()
            if(controller != null && controller!!.ready()) {
                controller?.changeRoom()
                //if(controller!!.ready()) throw DebugData.error("")
            }
        }

        fun draw(pos: PointN = PointN.ZERO) {
            drawNotActiveRooms(pos)
            active_room.draw(pos)

            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.stroke.rect(pos, active_room.size, Color.DARKBLUE)
        }

        private fun drawNotActiveRooms(pos: PointN) {
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            rooms.forEach { room ->
                graphics.stroke.rect(
                    room.pos - active_room.pos + pos,
                    room.size,
                    FGUtils.transparent(Color.LIGHTGRAY, 0.5))
            }
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
            World.controller?.init()
            rooms.clear()
            for(i in filename.indices) filenames.add(filename[i])
            filenames.forEach { name -> rooms.add(readInfo(name)) }
            set(0)
        }

        fun init(vararg filename: String) = init(null, *filename)

        fun set(id: Int) {
            active_room = rooms[id]
            active_id = id
        }

        fun reset() {
            respawn(active_id)
        }

        fun respawn(id: Int) {
            rooms[id] = readInfo(filenames[id])
            set(id)
        }

        fun add(o: GameObject) = active_room.add(o)
    }
}