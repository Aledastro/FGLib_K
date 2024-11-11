package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.controller.WorldController
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

class WorldLoadInfo(
    val rooms: Array<Room>,
    val getter: AbstractClassGetter<GameObject>,
    val controller: WorldController,
    val init_rooms: Boolean = true
) {
    val size
        get() = rooms.size

    constructor(
        filenames: Array<String>,
        getter: AbstractClassGetter<GameObject>,
        controller: WorldController,
        init_rooms: Boolean = true
    ): this(
        Array(filenames.size) { i -> WorldUtils.readInfo(getter, filenames[i]) }, getter, controller, init_rooms
    )
}
