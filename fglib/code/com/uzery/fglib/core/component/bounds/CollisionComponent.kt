package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.utils.math.geom.Shape

class CollisionComponent(c: BoundsComponent) {
    val element = c.element
    val code = c.code

    var shape: Shape? = null
}
