package com.uzery.fglib.core.room.entry

import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.getter.value.PosValue
import com.uzery.fglib.utils.data.getter.value.SizeValue
import com.uzery.fglib.utils.math.geom.PointN

data class FGRoomEntry(val pos: PointN, val size: PointN, val objs: ArrayList<FGEntry>) {
    override fun toString(): String {
        return buildString {
            append("room entry: ${PosValue(pos)} ${SizeValue(size)}\n\n")

            for (o in objs) {
                append("$o\n")
            }
        }
    }
}
