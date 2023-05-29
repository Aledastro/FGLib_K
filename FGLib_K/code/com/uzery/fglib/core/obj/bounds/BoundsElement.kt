package com.uzery.fglib.core.obj.bounds

import com.uzery.fglib.utils.math.geom.Shape

data class BoundsElement(val name: String, val shape: Shape) {
    constructor(shape: Shape): this("unnamed", shape)
}
