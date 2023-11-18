package com.uzery.fglib.core.obj.bounds

class BoundsBox {
    private val bounds = Array(SIZE) { Bounds() }
    operator fun get(index: Int) = bounds[index]

    val red: Bounds
        get() = bounds[RED]
    val orange: Bounds
        get() = bounds[ORANGE]
    val blue: Bounds
        get() = bounds[BLUE]
    val green: Bounds
        get() = bounds[GREEN]
    val main: Bounds
        get() = if (red.empty) orange else red


    companion object {
        enum class CODE { RED, ORANGE, BLUE, GREEN }

        fun name(index: Int) = CODE.values()[index].name
        fun index(name: String) = CODE.valueOf(name).ordinal

        val SIZE = CODE.values().size

        val RED = CODE.RED.ordinal
        val ORANGE = CODE.ORANGE.ordinal
        val BLUE = CODE.BLUE.ordinal
        val GREEN = CODE.GREEN.ordinal
    }
}