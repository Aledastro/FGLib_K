package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.image.WritableFGImage
import com.uzery.fglib.utils.math.geom.Direct

/**
 * TODO("doc")
 **/
object TurnDirect: ImageTransformEffect("turn_direct") {
    override operator fun get(origin: WritableFGImage, args: Array<Array<String>>): WritableFGImage {
        return when (val direct = Direct.valueOf(args[0][0])) {
            Direct.UP -> origin
            Direct.LEFT -> TurnCCW90[origin]
            Direct.DOWN -> ReverseXY[origin]
            Direct.RIGHT -> TurnCW90[origin]
            else -> throw DebugData.error("Can't rotate by diagonal direct: $direct")
        }
    }
}
