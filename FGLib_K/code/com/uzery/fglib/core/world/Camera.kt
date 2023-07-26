package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.geom.PointN

abstract class Camera: GameObject() {

    abstract fun drawPOS(): PointN
    abstract fun move(p: PointN)

    final override fun setValues() {
        name="temp"
    }
}
