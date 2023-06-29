package com.uzery.fglib.core.obj.bounds

class BoundsBox {
    val bounds = Array(SIZE) { Bounds() }
    operator fun get(index: Int) = bounds[index]

    val red: Bounds
        get() = bounds[CODE.RED.ordinal]
    val orange: Bounds
        get() = bounds[CODE.ORANGE.ordinal]
    val blue: Bounds
        get() = bounds[CODE.BLUE.ordinal]
    val green: Bounds
        get() = bounds[CODE.GREEN.ordinal]
    val main: Bounds
        get() = if(red.isEmpty()) orange else red


    companion object {
        enum class CODE { RED, ORANGE, BLUE, GREEN }

        fun name(index: Int) = CODE.values()[index].name
        fun index(name: String) = CODE.valueOf(name).ordinal

        val SIZE = CODE.values().size
    }
}