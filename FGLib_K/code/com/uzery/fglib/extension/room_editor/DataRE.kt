package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.geom.PointN

data class DataRE(
    val draw_pos: PointN,
    val edit: Room,
    val OFFSET: Double,
    val getter: ClassGetter<GameObject>,
    val GRID: Double,
    val GRID_P: PointN,
)