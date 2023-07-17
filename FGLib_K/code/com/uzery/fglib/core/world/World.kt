package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform.Companion.CANVAS_R
import com.uzery.fglib.core.program.Platform.Companion.develop_mode
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import java.util.*

interface World {
    companion object {
        val rooms = LinkedList<Room>()
        val active_rooms = LinkedList<Room>()
        private val last_active = LinkedList<Boolean>()

        private val filenames = LinkedList<String>()

        private lateinit var controller: WorldController

        fun allTagged(tag: String): List<GameObject> {
            val res = LinkedList<GameObject>()
            for(room in active_rooms) {
                res.addAll(room.objects.stream().filter { it.tagged(tag) }.toList())
            }
            return res
        }

        fun allExists(vararg tag: String) = tag.all { allTagged(it).isNotEmpty() }
        fun anyExists(vararg tag: String) = tag.any { allTagged(it).isNotEmpty() }

        fun noneExists(vararg tag: String) = !anyExists(*tag)

        var camera: Camera? = null

        fun next() {
            active_rooms.forEach { it.next() }

            controller.update()
            active_rooms.clear()
            for(id in rooms.indices) {
                if(controller.isActive(rooms[id])) {
                    if(!last_active[id]) controller.onAppear(rooms[id])
                    active_rooms.add(rooms[id])
                    last_active[id] = true
                } else {
                    if(last_active[id]) controller.onDisappear(rooms[id])
                    last_active[id] = false
                }
            }
            camera?.update()
            graphics.drawPOS = controller.drawPOS() + (camera?.drawPOS() ?: PointN.ZERO)

            WorldUtils.nextDebug()
            active_rooms.forEach { WorldUtils.nextDebugForRoom(it) }
        }

        fun draw(pos: PointN = PointN.ZERO) {
            drawNotActiveRooms(pos)
            drawRooms(pos)
            //if(develop_mode) drawRoomsDebug(pos)
        }

        private fun drawRooms(pos: PointN) {
            val vis = ArrayList<Visualiser>()
            val pos_map = HashMap<Visualiser, PointN>()
            active_rooms.forEach { room ->
                room.objects.forEach { obj ->
                    obj.visuals.forEach {
                        pos_map[it] = obj.stats.POS + room.pos
                        vis.add(it)
                    }
                }
            }
            Room.drawVisuals(pos, vis, pos_map)
        }

        private fun drawRoomsOld(pos: PointN) {
            active_rooms.forEach { it.draw(pos + it.pos) }
        }

        private fun drawRoomsDebug(pos: PointN) {
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            active_rooms.forEach { graphics.stroke.rect(pos + it.pos, it.size, Color.DARKBLUE) }

            active_rooms.forEach { WorldUtils.drawDebug(pos + it.pos, it) }
        }

        private fun drawNotActiveRooms(pos: PointN) {
            if(!develop_mode) return

            graphics.layer = DrawLayer.CAMERA_FOLLOW
            rooms.forEach { room ->
                graphics.stroke.rect(room.pos + pos, room.size, FGUtils.transparent(Color.LIGHTGRAY, 0.5))
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
                if(next.isNotEmpty()) objects.add(getter!![next])
            }

            return Room(pos, size, objects)
        }

        private fun getP(s: String): PointN {
            val c = object: ClassGetter<PointN>() {
                override fun addAll() = add("pos", 1) { pos }
            }
            return c["pos: $s"]
        }

        fun init(controller: WorldController, vararg filename: String) {
            World.controller = controller
            World.controller.init()
            rooms.clear()
            for(i in filename.indices) filenames.add(filename[i])
            filenames.forEach { rooms.add(readInfo(it)) }
            for(i in rooms.indices) last_active.add(false)
        }

        fun add(o: GameObject) {
            controller.roomFor(o).add(o)
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}