package com.uzery.fglib.core.obj.bounds

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
}
