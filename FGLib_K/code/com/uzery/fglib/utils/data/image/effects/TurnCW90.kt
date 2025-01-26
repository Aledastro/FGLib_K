package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.data.image.WritableFGImage
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
object TurnCW90: ImageTransformEffect("turnCW90") {
    override operator fun get(origin: WritableFGImage, args: Array<Array<String>>): WritableFGImage {
        val res = WritableFGImage(origin.size)
        for (pos in res.size.indices) {
            res.setArgb(pos, origin.getArgb(IntI(pos.y, origin.size.width-1-pos.x)))
        }
        return res
    }
}
