package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * Geometric union of [BoundsElement]
 **/
class Bounds(vararg els: BoundsElement) {
    val elements = ArrayList<BoundsElement>()

    init {
        add(*els)
    }

    constructor(group: String, name: String, shape: () -> Shape?): this(BoundsElement(group, name, shape))
    constructor(name: String, shape: () -> Shape?): this(BoundsElement(name, shape))
    constructor(shape: () -> Shape?): this(BoundsElement(shape))

    fun add(vararg els: BoundsElement) = els.forEach { element -> elements.add(element) }

    private fun main(): RectN? {
        if (empty) return null

        val list = ArrayList<Shape>()
        elements.forEach { el -> el.now?.let { list.add(it) } }

        if (list.isEmpty()) return null
        return ShapeUtils.mainOf(*list.toTypedArray())
    }

    val empty
        get() = elements.isEmpty()

    fun into(pos: PointN): Boolean {
        return elements.any { it.now?.into(pos) == true }
    }

    var main: RectN? = null
    fun next() {
        elements.forEach { it.next() }
        main = main()
    }
}
