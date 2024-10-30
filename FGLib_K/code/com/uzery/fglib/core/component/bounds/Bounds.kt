package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.max
import kotlin.math.min

class Bounds {
    val elements = ArrayList<BoundsElement>()

    fun add(vararg els: BoundsElement) = els.forEach { element -> elements.add(element) }
    fun add(vararg shapes: () -> Shape?) = shapes.forEach { shape -> elements.add(BoundsElement(shape)) }
    fun add(shape: () -> Shape?) = elements.add(BoundsElement(shape))
    fun add(name: String, shape: () -> Shape?) = elements.add(BoundsElement(name, shape))

    constructor()

    constructor(vararg els: BoundsElement) {
        add(*els)
    }

    constructor(vararg shape: () -> Shape?) {
        add(*shape)
    }

    constructor(shape: () -> Shape?) {
        add(shape)
    }

    constructor(name: String, shape: () -> Shape?) {
        add(name, shape)
    }

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

    @Deprecated("It doesn't copy original manually")
    fun copy(pos: PointN): Bounds {
        val els = ArrayList<BoundsElement>()
        elements.forEach { els.add(it.copy(pos)) }
        return Bounds(*els.toTypedArray())
    }

    fun into(pos: PointN): Boolean {
        return elements.any { it.shape()?.into(pos) == true }
    }
}
