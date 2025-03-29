package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
class OneRoomWC: CameraWorldController() {
    var room: Room? = null
    private val void = Room("void", PointN.ZERO, PointN.ZERO)
    override fun roomFor(world: World, obj: GameObject): Room {
        return room ?: void
    }

    override fun isActive(world: World, r: Room) = true
    override fun onDisappear(world: World, r: Room) {}

    override fun onAppear(world: World, r: Room) {}

    override fun init(world: World) {}

    override fun update0(world: World) {
        void.next()
    }

    override fun drawPOS0(): PointN {
        return PointN.ZERO
    }

    override fun draw(world: World, pos: PointN) {}
}
