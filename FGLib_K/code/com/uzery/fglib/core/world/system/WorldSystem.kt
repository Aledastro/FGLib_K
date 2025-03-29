package com.uzery.fglib.core.world.system

import com.uzery.fglib.core.world.World

abstract class WorldSystem {
    open val priority = 0

    open fun init(world: World) {}
    abstract fun update(world: World)
}
