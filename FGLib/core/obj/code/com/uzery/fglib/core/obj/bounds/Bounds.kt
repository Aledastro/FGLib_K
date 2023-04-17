package com.uzery.fglib.core.obj.bounds

import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.geom.Shape

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
        var min = elements[0].shape.L
        var max = elements[0].shape.R
        for(el in elements) {
            if(el.shape.L.less(min)) min = el.shape.L
            if(el.shape.R.more(min)) max = el.shape.R
        }
        return RectN.rectLR(min, max)
    }
}
