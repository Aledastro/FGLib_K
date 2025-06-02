package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.core.obj.GameObject

/**
 * RED, ORANGE, BLUE, GREEN and GRAY [Bounds] for [GameObject]
 **/
class BoundsBox {
    private val bounds = Array(SIZE) { Bounds() }
    operator fun get(index: Int) = bounds[index]

    fun next() {
        bounds.forEach { it.next() }
    }

    val red: Bounds
        get() = bounds[RED]
    val orange: Bounds
        get() = bounds[ORANGE]
    val blue: Bounds
        get() = bounds[BLUE]
    val green: Bounds
        get() = bounds[GREEN]
    val gray: Bounds
        get() = bounds[GRAY]

    companion object {
        enum class CODE { RED, ORANGE, BLUE, GREEN, GRAY }

        fun name(index: Int) = CODE.entries[index].name
        fun index(name: String) = CODE.valueOf(name).ordinal

        val SIZE = CODE.entries.size

        val RED = CODE.RED.ordinal
        val ORANGE = CODE.ORANGE.ordinal
        val BLUE = CODE.BLUE.ordinal
        val GREEN = CODE.GREEN.ordinal
        val GRAY = CODE.GRAY.ordinal

        val indices
            get() = CODE.entries.indices
    }
}
