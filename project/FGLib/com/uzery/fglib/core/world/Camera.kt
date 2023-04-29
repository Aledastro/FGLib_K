package com.uzery.fglib.core.world

import com.uzery.fglib.utils.math.geom.PointN

interface Camera {

    fun update() {
        /* ignore */
    }

    fun drawPOS(): PointN
}
