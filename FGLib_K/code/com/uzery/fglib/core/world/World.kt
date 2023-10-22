package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform.develop_mode
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.WorldUtils.readInfo
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import java.util.*

object World {
    val rooms = LinkedList<Room>()
    val active_rooms = LinkedList<Room>()
    private val last_active = LinkedList<Boolean>()

    private val filenames = LinkedList<String>()

    private lateinit var controller: WorldController

    fun allTagged(tag: String): List<GameObject> {
        val res = LinkedList<GameObject>()
        for (room in active_rooms) {
            res.addAll(room.objects.stream().filter { it.tagged(tag) }.toList())
        }
        return res
    }

    fun allExists(vararg tag: String) = tag.all { allTagged(it).isNotEmpty() }
    fun anyExists(vararg tag: String) = tag.any { allTagged(it).isNotEmpty() }

    fun noneExists(vararg tag: String) = !anyExists(*tag)

    var camera: Camera? = null

    fun next() {
        controller.update()
        active_rooms.clear()
        for (id in rooms.indices) {
            if (controller.isActive(rooms[id])) {
                if (!last_active[id]) controller.onAppear(rooms[id])
                active_rooms.add(rooms[id])
                last_active[id] = true
            } else {
                if (last_active[id]) controller.onDisappear(rooms[id])
                last_active[id] = false
            }
        }
        active_rooms.forEach { it.next() }

        camera?.next()

        WorldUtils.nextDebug()
        active_rooms.forEach { WorldUtils.nextDebugForRoom(it) }
    }

    fun draw(pos: PointN = PointN.ZERO) {
        graphics.drawPOS = controller.drawPOS()+(camera?.drawPOS() ?: PointN.ZERO)
        drawNotActiveRooms(pos)
        drawRooms(pos)
        camera?.draw(camera!!.stats.POS+camera!!.stats.roomPOS+pos)
        //if(develop_mode) drawRoomsDebug(pos)
    }

    private fun drawRooms(pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val pos_map = HashMap<Visualiser, PointN>()
        val sort_map = HashMap<Visualiser, PointN>()
        active_rooms.forEach { room ->
            room.objects.forEach { obj ->
                obj.visuals.forEach {
                    pos_map[it] = obj.stats.POS+room.pos
                    sort_map[it] = pos_map[it]!!+obj.stats.sortPOS
                    vis.add(it)
                }
            }
        }
        Room.drawVisuals(pos, vis, pos_map, sort_map)
    }

    private fun drawRoomsOld(pos: PointN) {
        active_rooms.forEach { it.draw(pos+it.pos) }
    }

    private fun drawRoomsDebug(pos: PointN) {
        graphics.layer = DrawLayer.CAMERA_FOLLOW
        active_rooms.forEach { graphics.stroke.rect(pos+it.pos, it.size, Color.DARKBLUE) }

        active_rooms.forEach { WorldUtils.drawDebug(pos+it.pos, it) }
    }

    private fun drawNotActiveRooms(pos: PointN) {
        if (!develop_mode) return

        graphics.layer = DrawLayer.CAMERA_FOLLOW
        rooms.forEach { room ->
            graphics.stroke.rect(room.pos+pos, room.size, FGUtils.transparent(Color.LIGHTGRAY, 0.5))
        }
    }

    var getter: ClassGetter<GameObject>? = null

    fun init(controller: WorldController, vararg filename: String) {
        World.controller = controller
        World.controller.init()
        rooms.clear()
        active_rooms.clear()
        filenames.clear()
        camera = null
        for (name in filename) filenames.add(name)
        filenames.forEach { rooms.add(readInfo(it)) }
        for (i in rooms.indices) last_active.add(false)
    }

    fun add(o: GameObject) {
        controller.roomFor(o).add(o)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
}