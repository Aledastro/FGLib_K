package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.camera.Camera
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class CameraWorldController: WorldController {
    var camera: Camera? = null

    final override fun drawPOS(world: World): PointN {
        return drawPOS0()+(camera?.drawPOS() ?: PointN.ZERO)
    }

    abstract fun drawPOS0(): PointN
}
