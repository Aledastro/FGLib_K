package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.data.image.WritableFGImage
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
object TurnCCW90: ImageTransformEffect("turnCCW90") {
    override operator fun get(origin: WritableFGImage, args: Array<Array<String>>): WritableFGImage {
        val res = WritableFGImage(IntI(origin.size.width, origin.size.height))
        for (pos in res.size.indices) {
            res.setArgb(pos, origin.getArgb(IntI(origin.size.height-1-pos.y, pos.x)))
        }
        return res
    }
}
