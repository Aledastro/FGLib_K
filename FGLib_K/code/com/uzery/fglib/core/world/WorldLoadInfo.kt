package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.world.controller.WorldController
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

class WorldLoadInfo(
    val filenames: Array<String>,
    val getter: AbstractClassGetter<GameObject>,
    val controller: WorldController,
    val init_rooms: Boolean = true
) {
    val size
        get() = filenames.size
}
