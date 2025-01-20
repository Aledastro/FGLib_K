package com.uzery.fglib.core.room.entry

import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.math.geom.PointN

data class FGRoomEntry(val pos: PointN, val size: PointN, val objs: ArrayList<FGEntry>) {
    override fun toString(): String {
        return FGRoomFormat.stringFrom(this)
    }
}
