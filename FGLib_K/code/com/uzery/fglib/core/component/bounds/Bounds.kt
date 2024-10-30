package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.max
import kotlin.math.min

class Bounds(vararg els: BoundsElement) {
    init {
        add(*els)
    }

    val elements = ArrayList<BoundsElement>()

    fun add(vararg els: BoundsElement) = els.forEach { element -> elements.add(element) }

    fun main(): RectN? {
        if (empty) return null

        lateinit var min: PointN
        lateinit var max: PointN
        var first = true

        for (element in elements) {
            val shape = element.shape() ?: continue
            if (first) {
                min = shape.L
                max = shape.R
                first = false
            }
            min = PointN.transform(min, shape.L) { a, b -> min(a, b) }
            max = PointN.transform(max, shape.R) { a, b -> max(a, b) }
        }

        if (first) return null
        return RectN.LR(min, max)
    }

    val empty
        get() = elements.isEmpty()

    fun into(pos: PointN): Boolean {
        return elements.any { it.shape()?.into(pos) == true }
    }
}
