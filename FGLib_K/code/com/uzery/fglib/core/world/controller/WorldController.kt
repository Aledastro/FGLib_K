package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
interface WorldController {
    fun roomFor(world: World, obj: GameObject): Room

    fun isActive(world: World, r: Room): Boolean

    fun onDisappear(world: World, r: Room)
    fun onAppear(world: World, r: Room)

    fun init(world: World)

    fun update(world: World)
    fun drawPOS(world: World): PointN

    fun draw(world: World, pos: PointN)
    fun drawAfter(world: World, pos: PointN)
}
