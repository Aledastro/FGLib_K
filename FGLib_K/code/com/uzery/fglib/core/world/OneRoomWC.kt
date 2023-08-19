package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.math.geom.PointN

class OneRoomWC: WorldController {
    var room: Room? = null
    private val void = Room(PointN.ZERO, PointN.ZERO)
    override fun roomFor(obj: GameObject): Room {
        return room ?: void
    }

    override fun isActive(r: Room) = true
    override fun onDisappear(r: Room) {}

    override fun onAppear(r: Room) {}

    override fun init() {}

    override fun update() {
        void.next()
    }

    override fun drawPOS(): PointN {
        return PointN.ZERO
    }
}