package com.uzery.fglib.core.world

import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.room.RoomDrawUtils
import com.uzery.fglib.core.world.controller.WorldController
import com.uzery.fglib.core.world.system.WorldSystem
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
class World {
    val rooms = ArrayList<Room>()
    val active_rooms = ArrayList<Room>()
    private val last_active = HashMap<Room, Boolean>()

    lateinit var controller: WorldController
    private lateinit var systems: Array<WorldSystem>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun init(controller: WorldController, systems: Array<WorldSystem>, rooms: Array<Room>, init_rooms: Boolean = true) {
        this.controller = controller
        controller.init(this)

        this.rooms.addAll(rooms)

        if (init_rooms) rooms.forEach { it.init() }

        this.systems = systems
        systems.forEach { it.init(this) }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun next() {
        controller.update(this)

        active_rooms.clear()
        for (room in rooms) {
            if (controller.isActive(this, room)) {
                if (last_active[room] != true) controller.onAppear(this, room)
                active_rooms.add(room)
                last_active[room] = true
            } else {
                if (last_active[room] == true) controller.onDisappear(this, room)
                last_active[room] = false
            }
        }

        systems
            .filter { it.priority<0 }
            .sortedBy { it.priority }
            .forEach { it.update(this) }

        active_rooms.forEach { it.next() }

        systems
            .filter { it.priority>=0 }
            .sortedBy { it.priority }
            .forEach { it.update(this) }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN = PointN.ZERO) {
        val drawPOS = pos-controller.drawPOS(this)

        controller.draw(this, drawPOS)
        drawRooms(drawPOS)
        controller.drawAfter(this, drawPOS)
    }

    private fun drawRooms(pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val pos_map = HashMap<Visualiser, PointN>()
        val sort_map = HashMap<Visualiser, PointN>()
        active_rooms.forEach { room ->
            room.objects.forEach { obj ->
                RoomDrawUtils.addObjVis(vis, pos_map, sort_map, obj)
            }
        }
        RoomDrawUtils.drawVisuals(pos, vis, pos_map, sort_map)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun add(o: GameObject) {
        controller.roomFor(this, o).add(o)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun allMatch(predicate: (GameObject) -> Boolean): List<GameObject> {
        val res = ArrayList<GameObject>()
        for (room in rooms) {
            res.addAll(room.objects.filter { obj -> predicate(obj) })
        }
        return res
    }

    fun allTagged(tag: String) = allMatch { obj -> obj.tagged(tag) }

    fun find(predicate: (GameObject) -> Boolean): Pair<Room, GameObject>? {
        for (room in rooms) {
            for (o in room.objects) {
                if (predicate(o)) return Pair(room, o)
            }
        }
        return null
    }
}
