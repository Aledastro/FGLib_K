package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.utils.math.geom.Shape

/**
 * [Bounds] element of one [Shape]
 *
 * @property name element name
 * @property shape shape builder
 **/
data class BoundsElement(val name: String, private val shape: () -> Shape?) {
    constructor(shape: () -> Shape?): this(DEFAULT_NAME, shape)

    var now = shape()
        private set

    fun next() {
        now = shape()
    }

    companion object {
        const val DEFAULT_NAME = "unnamed"
    }
}
