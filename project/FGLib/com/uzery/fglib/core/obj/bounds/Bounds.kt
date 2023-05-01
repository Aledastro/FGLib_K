package com.uzery.fglib.core.obj.bounds

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.geom.Shape
import kotlin.math.max
import kotlin.math.min

data class Bounds(val elements: Array<BoundsElement>) {
    constructor(vararg shapes: Shape): this(Array(shapes.size) { i -> BoundsElement(shapes[i]) })

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Bounds

        if(!elements.contentEquals(other.elements)) return false

        return true
    }

    override fun hashCode(): Int {
        return elements.contentHashCode()
    }

    fun main(): RectN {
        var min = elements.first().shape.L
        var max = elements.first().shape.R
        for(el in elements) {
            min = PointN.transform(min, el.shape.L) { a, b -> min(a, b) }
            max = PointN.transform(max, el.shape.R) { a, b -> max(a, b) }
        }
        return RectN.rectLR(min, max)
    }
}
