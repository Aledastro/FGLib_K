package com.uzery.fglib.core.world.camera

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class Camera: GameObject() {
    init {
        tag("migrator")
    }

    open fun drawPOS() = stats.POS-CANVAS/2
    abstract fun move(p: PointN)
}
