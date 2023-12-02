package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.image.WritableFGImage
import com.uzery.fglib.utils.math.MathUtils
import javafx.scene.image.WritableImage

object TurnCCW: ImageTransformEffect("turnCCW") {
    override operator fun get(origin: WritableFGImage, args: List<List<String>>): WritableFGImage {
        return when (val degree = MathUtils.mod(args[0][0].toInt(), 360)) {
            0 -> origin
            90 -> TurnCCW90[origin]
            180 -> ReverseXY[origin]
            270 -> TurnCW90[origin]
            else -> throw DebugData.error("Can't rotate on custom degree yet: $degree")
        }
    }
}
