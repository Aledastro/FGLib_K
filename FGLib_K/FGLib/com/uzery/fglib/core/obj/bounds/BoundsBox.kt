package com.uzery.fglib.core.obj.bounds

class BoundsBox {
    val bounds = Array<(() -> Bounds)?>(SIZE) { null }
    operator fun get(index: Int) = bounds[index]

    var red: (() -> Bounds)?
        get() = bounds[CODE.RED.ordinal]
        set(value) {
            bounds[CODE.RED.ordinal] = value
        }
    var orange: (() -> Bounds)?
        get() = bounds[CODE.ORANGE.ordinal]
        set(value) {
            bounds[CODE.ORANGE.ordinal] = value
        }
    var blue: (() -> Bounds)?
        get() = bounds[CODE.BLUE.ordinal]
        set(value) {
            bounds[CODE.BLUE.ordinal] = value
        }
    var green: (() -> Bounds)?
        get() = bounds[CODE.GREEN.ordinal]
        set(value) {
            bounds[CODE.GREEN.ordinal] = value
        }
    val main: (() -> Bounds)?
        get() = if(red != null) red else orange


    companion object {
        private enum class CODE { RED, ORANGE, BLUE, GREEN }

        fun name(index: Int) = CODE.values()[index].name
        fun index(name: String) = CODE.valueOf(name).ordinal

        val SIZE = CODE.values().size
    }
}