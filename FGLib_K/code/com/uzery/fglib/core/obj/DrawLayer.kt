package com.uzery.fglib.core.obj

import com.uzery.fglib.utils.data.getter.value.DrawLayerValue
import com.uzery.fglib.utils.graphics.AffineGraphics

/**
 * @property z [AffineGraphics] drawPOS change value
 * @property sort to compare draw order with another [DrawLayer]
 * @property name for export in [DrawLayerValue]
 **/
data class DrawLayer(val z: Double, val sort: Double = z, val name: String = "unnamed") {
    constructor(z: Double, sort: Int, name: String): this(z, sort.toDouble(), name)

    companion object {
        val CAMERA_OFF = DrawLayer(0.0)
        val CAMERA_FOLLOW = DrawLayer(1.0)
    }
}
