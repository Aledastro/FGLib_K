package com.uzery.fglib.utils.input

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN

abstract class TouchActivator(screen: RectN) {
    abstract fun pos0(): PointN
    fun pos(): PointN {
        return pos0()
    }
}
