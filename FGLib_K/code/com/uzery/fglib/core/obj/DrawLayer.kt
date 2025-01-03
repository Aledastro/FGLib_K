package com.uzery.fglib.core.obj

import com.uzery.fglib.utils.data.getter.value.DrawLayerValue
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN

/**
 * @property z [AffineGraphics] drawPOS change value
 * @property sort to compare draw order with another [DrawLayer]
 * @property name for export in [DrawLayerValue]
 **/
data class DrawLayer(val z: PointN, val sort: Double, val name: String = "unnamed") {
    companion object {
        val CAMERA_OFF = DrawLayer(PointN(0.0, 0.0), 0.0, "UTIL_CAMERA_OFF")
        val CAMERA_FOLLOW = DrawLayer(PointN(1.0, 1.0),1.0, "UTIL_CAMERA_FOLLOW")
    }
}
