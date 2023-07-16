package com.uzery.fglib.core.obj.bounds

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

data class BoundsElement(val name: String, val shape: () -> Shape?) {
    constructor(shape: () -> Shape?): this("unnamed", shape)

    fun copy(pos: PointN): BoundsElement{
        return BoundsElement(name) { shape.invoke()?.copy(pos) }
    }
}
