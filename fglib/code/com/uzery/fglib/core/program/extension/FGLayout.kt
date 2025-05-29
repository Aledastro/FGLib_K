package com.uzery.fglib.core.program.extension

import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
enum class FGLayout(val value: PointN) {
    TOP_LEFT(PointN(0.0, 0.0)),
    TOP(PointN(0.5, 0.0)),
    TOP_RIGHT(PointN(1.0, 0.0)),

    LEFT(PointN(0.0, 0.5)),
    CENTER(PointN(0.5, 0.5)),
    RIGHT(PointN(1.0, 0.5)),

    BOTTOM_LEFT(PointN(0.0, 1.0)),
    BOTTOM(PointN(0.5, 1.0)),
    BOTTOM_RIGHT(PointN(1.0, 1.0)),
}
