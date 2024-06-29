package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.data.image.WritableFGImage
import com.uzery.fglib.utils.struct.num.IntI

object ReverseX: ImageTransformEffect("reverseX") {
    override operator fun get(origin: WritableFGImage, args: List<List<String>>): WritableFGImage {
        val res = WritableFGImage(origin.size)
        for (pos in res.size.indices) {
            res.setArgb(pos, origin.getArgb(IntI(origin.size.width-1-pos.x, pos.y)))
        }
        return res
    }
}
