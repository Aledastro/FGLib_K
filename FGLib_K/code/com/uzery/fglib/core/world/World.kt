package com.uzery.fglib.core.world

import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.room.RoomUtils
import com.uzery.fglib.core.world.controller.WorldController
import com.uzery.fglib.utils.math.geom.PointN

class World {
    val rooms = ArrayList<Room>()
    val active_rooms = ArrayList<Room>()
    private val last_active = HashMap<Room, Boolean>()

    lateinit var controller: WorldController

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun init(rooms: Array<Room>, controller: WorldController, init_rooms: Boolean = true) {
        this.controller = controller
        controller.init()

        this.rooms.addAll(rooms)

        if (init_rooms) rooms.forEach { it.init() }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun next() {
        controller.update()
        active_rooms.clear()
        for (room in rooms) {
            if (controller.isActive(room)) {
                if (last_active[room] != true) controller.onAppear(room)
                active_rooms.add(room)
                last_active[room] = true
            } else {
                if (last_active[room] == true) controller.onDisappear(room)
                last_active[room] = false
            }
        }
        active_rooms.forEach { it.next() }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN = PointN.ZERO) {
        val drawPOS = pos-controller.drawPOS()

        controller.draw(drawPOS)
        drawRooms(drawPOS)
        controller.drawAfter(drawPOS)
    }

    private fun drawRooms(pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val pos_map = HashMap<Visualiser, PointN>()
        val sort_map = HashMap<Visualiser, PointN>()
        active_rooms.forEach { room ->
            room.objects.forEach { obj ->
                RoomUtils.addObjVis(vis, pos_map, sort_map, obj)
            }
        }
        RoomUtils.drawVisuals(pos, vis, pos_map, sort_map)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun add(o: GameObject) {
        controller.roomFor(o).add(o)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun allMatch(predicate: (GameObject) -> Boolean): List<GameObject> {
        val res = ArrayList<GameObject>()
        for (room in active_rooms) {
            res.addAll(room.objects.filter { obj -> predicate(obj) })
        }
        return res
    }

    fun allTagged(tag: String) = allMatch { obj -> obj.tagged(tag) }
}
