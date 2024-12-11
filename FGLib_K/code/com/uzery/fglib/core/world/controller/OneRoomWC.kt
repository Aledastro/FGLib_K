package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
class OneRoomWC: CameraWorldController() {
    var room: Room? = null
    private val void = Room(PointN.ZERO, PointN.ZERO)
    override fun roomFor(obj: GameObject): Room {
        return room ?: void
    }

    override fun isActive(r: Room) = true
    override fun onDisappear(r: Room) {}

    override fun onAppear(r: Room) {}

    override fun init() {}

    override fun update0() {
        void.next()
    }

    override fun drawPOS0(): PointN {
        return PointN.ZERO
    }

    override fun draw(pos: PointN) {}
}
