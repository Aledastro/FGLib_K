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
    var room: Room? = null
    private val void = Room("void", PointN.ZERO, PointN.ZERO)
    override fun roomFor(world: World, obj: GameObject): Room {
        return room ?: void
    }

    override fun isActive(world: World, r: Room) = true

    override fun onDisappear(world: World, r: Room) {}
    override fun onAppear(world: World, r: Room) {}

    override fun init(world: World) {
    }

    override fun update(world: World) {
        initRoom(world)

        void.next()
        if (camera == null && room != null) {
            room!!.objects.find { it is Camera }?.let { camera = it as Camera }
        }
    }

    override fun drawPOS0(): PointN {
        return PointN.ZERO
    }

    override fun draw(world: World, pos: PointN) {}
    override fun drawAfter(world: World, pos: PointN) {}

    private fun initRoom(world: World) {
        if (room != null) return
        val p = world.find { o -> o.tagged(UtilTags.util_wc_goal) } ?: return
        room = p.first
    }
}
