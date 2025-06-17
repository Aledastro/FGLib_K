package com.uzery.fglib.core.world

import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.DebugTools.countTime
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

    val draw_pos
        get() = -controller.drawPOS(this)

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun init(controller: WorldController, systems: Array<WorldSystem>, rooms: Array<Room>, init_rooms: Boolean = true) {
        this.controller = controller
        controller.init(this)

        this.rooms.addAll(rooms)

        if (init_rooms) rooms.forEach { it.init() }

        this.systems = systems
        systems.forEach { it.init(this) }

        name = "world"+this.rooms.map { it.name } //todo?
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun next() {
        nextLogics()
        nextTime()
    }

    private lateinit var name: String

    private fun nextLogics() {
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
            .filter { it.priority < 0 }
            .sortedBy { it.priority }
            .forEach { it.update(this) }

        countTime("world | update", name) {
            active_rooms.forEach { it.nextLogics() }
        }

        systems
            .filter { it.priority >= 0 }
            .sortedBy { it.priority }
            .forEach { it.update(this) }
    }

    private fun nextTime() {
        active_rooms.forEach { it.nextTime() }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN = PointN.ZERO) {
        countTime("world | draw", name) {
            val drawPOS = pos+draw_pos

            controller.draw(this, drawPOS)
            drawRooms(drawPOS)
            controller.drawAfter(this, drawPOS)
        }
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
        return rooms.flatMap { room -> room.objects.filter { obj -> predicate(obj) } }
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
