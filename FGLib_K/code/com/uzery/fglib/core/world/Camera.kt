package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.geom.PointN

abstract class Camera: GameObject() {
    init {
        //tag("migrator")
    }

    open fun drawPOS() = stats.POS-Platform.CANVAS/2
    abstract fun move(p: PointN)

    final override fun setValues() {
        name = "temp"
    }
}
