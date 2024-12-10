package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.utils.math.geom.Shape

data class BoundsElement(var group: String, val name: String, private val shape: () -> Shape?) {
    constructor(name: String, shape: () -> Shape?): this(DEFAULT_GROUP, name, shape)
    constructor(shape: () -> Shape?): this(DEFAULT_GROUP, DEFAULT_NAME, shape)

    var now = shape()
    fun next() {
        now = shape()
    }

    companion object {
        private const val DEFAULT_GROUP = "global"
        const val DEFAULT_NAME = "unnamed"
    }
}
