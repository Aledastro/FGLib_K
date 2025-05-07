package com.uzery.fglib.core.world.system

import com.uzery.fglib.core.program.DebugTools.countTime
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World

abstract class WorldSystem {
    open val priority = 0

    open fun init(world: World) {}

    private val java_code = this.javaClass.name

    internal fun update(world: World) {
        countTime("world | update", "system: $java_code") {
            updateWorld(world)
            world.rooms.forEach { room -> updateRoom(room) }
        }
    }

    open fun updateWorld(world: World) {}
    open fun updateRoom(room: Room) {}
}
