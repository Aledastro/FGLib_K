package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.data.image.WritableFGImage
import com.uzery.fglib.utils.graphics.data.FGColor

/**
 * TODO("doc")
 **/
object UniColor: ImageTransformEffect("uni_color") {
    override operator fun get(origin: WritableFGImage, args: List<List<String>>): WritableFGImage {
        val color = FGColor.web(args[0][0])

        val res = WritableFGImage(origin.size)
        for (pos in res.size.indices) {
            val rgb = origin.getColor(pos)
            res.setColor(pos, if (rgb == FGColor.TRANSPARENT) rgb else color)
        }
        return res
    }
}
