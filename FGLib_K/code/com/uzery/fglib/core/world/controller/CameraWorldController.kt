package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.world.camera.Camera
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class CameraWorldController: WorldController {
    var camera: Camera? = null

    final override fun update() {
        camera?.next()
        update0()
    }

    open fun update0() {}

    final override fun drawAfter(pos: PointN) {
        camera?.draw(camera!!.stats.realPOS+pos)
        drawAfter0(pos)
    }

    open fun drawAfter0(pos: PointN) {}

    final override fun drawPOS(): PointN {
        return drawPOS0()+(camera?.drawPOS() ?: PointN.ZERO)
    }

    abstract fun drawPOS0(): PointN
}
