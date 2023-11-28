package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.math.geom.PointN

data class EditAction(val code: EditActionCode, val list: List<Pair<GameObject, Room>>, val pos: PointN = PointN.ZERO) {
    override fun toString(): String {
        val res = ArrayList<String>()
        list.forEach { res.add("[obj: ${it.first}, room: ${it.second.main}]") }
        return "EditAction: code[${code.name}], list: $res, pos: $pos"
    }
}
