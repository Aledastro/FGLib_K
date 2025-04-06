package com.uzery.fglib.core.world.system

import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World

abstract class WorldSystem {
    open val priority = 0

    open fun init(world: World) {}

    internal fun update(world: World) {
        updateWorld(world)
        world.rooms.forEach { room -> updateRoom(room) }
    }

    open fun updateWorld(world: World) {}
    open fun updateRoom(room: Room) {}
}
