package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN

class Bounds(vararg els: BoundsElement) {
    init {
        add(*els)
    }

    val elements = ArrayList<BoundsElement>()

    fun add(vararg els: BoundsElement) = els.forEach { element -> elements.add(element) }

    fun main(): RectN? {
        if (empty) return null

        val list = ArrayList<Shape>()
        elements.forEach { el -> el.shape()?.let { list.add(it) } }

        if (list.isEmpty()) return null
        return ShapeUtils.mainOf(*list.toTypedArray())
    }

    val empty
        get() = elements.isEmpty()

    fun into(pos: PointN): Boolean {
        return elements.any { it.shape()?.into(pos) == true }
    }
}
