package com.uzery.fglib.core.obj

import com.uzery.fglib.utils.math.geom.PointN

abstract class ObjectTransform(private val o: GameObject) {
    open val move: (d_pos: PointN)->Unit = { d_pos->
        pos += d_pos
    }
    open val resize: (d_size: PointN)->Unit = { d_size->
        pos += d_size
    }
    open val addIn: (pos: PointN)->Unit = {}
    open val removeIn: (pos: PointN)->Unit = {}
    open val turnTo: (d: Double)->Unit = {}

    var pos = PointN.ZERO
        get() = o.stats.POS
        private set(value){
            o.stats.POS = value
            field = value
        }
    var size = PointN.ZERO
        get() = o.stats.SIZE
        private set(value){
            o.stats.SIZE = value
            field = value
        }
}
