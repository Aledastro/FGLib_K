package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.struct.num.IntI
import kotlin.math.min

object ImageUtils {
    fun combination(
        size: IntI,
        images: Array<FGImage>,
        positions: Array<IntI> = Array(images.size) { IntI() },
    ): FGImage {
        val res = WritableFGImage(size)
        for (i in images.indices) {
            draw(res, images[i], positions[i])
        }
        return res.toFGImage()
    }

    fun draw(origin: WritableFGImage, image: FGImage, pos: IntI) {
        val size = IntI(
            min(origin.size.width-pos.x, origin.size.width),
            min(origin.size.height-pos.y, origin.size.height)
        )
        for (id in size.indices) {
            if (image.getColor(id) == FGColor.TRANSPARENT) continue
            origin.setArgb(pos+id, image.getArgb(id))
        }
    }

    fun split(origin: FGImage, pos: IntI, size: IntI): FGImage {
        val res = WritableFGImage(size)
        res.setPixels(pos, size, origin)
        return res.toFGImage()
    }

    fun scale(origin: FGImage, scale: IntI): FGImage {
        val res = WritableFGImage(scale*origin.size)
        for (pos in res.size.indices) {
            res.setArgb(pos, origin.getArgb(pos/scale))
        }
        return res.toFGImage()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun from(path: String): FGImage {
        return FGImage("file:"+ImageData.resolvePath(path))
    }

    fun from(path: String, vararg effects: String): FGImage {
        return from(from(path), *effects)
    }

    fun from(origin: FGImage, vararg effects: String): FGImage {
        if (effects.isEmpty()) return origin

        var res = WritableFGImage(origin.size)
        res.draw(origin)
        effects.forEach { res = ImageTransformUtils.applyEffect(res, it) }

        return res.toFGImage()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
