package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.math.geom.PointN

interface WorldController {
    fun roomFor(obj: GameObject): Room

    fun isActive(r: Room): Boolean

    fun onDisappear(r: Room)
    fun onAppear(r: Room)

    fun init()

    fun update()
    fun drawPOS(): PointN

    fun draw(pos: PointN)
    fun drawAfter(pos: PointN)
}
