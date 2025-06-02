package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.UtilTags
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.camera.Camera
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
class MigrateRoomWC: CameraWorldController() {
    private var goal_room: Room? = null
    private val void = Room("void", PointN.ZERO, PointN.ZERO)
    override fun roomFor(world: World, obj: GameObject): Room {
        return world.rooms.firstOrNull { isInArea(it, obj) } ?: void
    }

    private fun isInArea(r: Room, obj: GameObject): Boolean {
        return r.area.into(obj.stats.realPOS)
    }

    override fun isActive(world: World, r: Room) = true

    override fun onDisappear(world: World, r: Room) {}
    override fun onAppear(world: World, r: Room) {}

    override fun init(world: World) {
    }

    override fun update(world: World) {
        initRoom(world)

        void.next()
        setCamera()

        moveGoal(world)
    }

    private fun setCamera() {
        if (camera == null && goal_room != null) {
            goal_room!!.objects.find { it is Camera }?.let { camera = it as Camera }
        }
    }

    private fun moveGoal(world: World) {
        val goal = goal ?: return
        val goal_room = goal_room ?: return

        val new_room = roomFor(world, goal)
        if (goal_room == new_room) return

        goal.stats.POS += goal_room.pos-new_room.pos
        goal.stats.roomPOS = new_room.pos
        camera?.stats?.roomPOS = goal.stats.roomPOS
        camera?.move(goal_room.pos-new_room.pos)

        goal_room.objects.remove(goal)
        new_room.objects.add(goal)

        this.goal_room = new_room
    }

    override fun drawPOS0(): PointN {
        return goal_room?.pos ?: PointN.ZERO
    }

    override fun draw(world: World, pos: PointN) {}
    override fun drawAfter(world: World, pos: PointN) {}

    private var goal: GameObject? = null
    private fun initRoom(world: World) {
        if (goal_room != null) return
        val p = world.find { o -> o.tagged(UtilTags.util_wc_goal) } ?: return
        goal_room = p.first
        goal = p.second
    }
}
