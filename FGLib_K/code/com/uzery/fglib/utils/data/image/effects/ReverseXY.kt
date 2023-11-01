package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.WritableImage

object ReverseXY: ImageTransformEffect("reverseXY") {
    override operator fun get(origin: WritableImage, args: List<List<String>>): WritableImage {
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

        val res = WritableImage(origin_size.width, origin_size.height)
        for (pos in origin_size.indices) {
            res.pixelWriter.setArgb(pos.x, pos.y,
                origin.pixelReader.getArgb(origin_size.width-1-pos.x, origin_size.height-1-pos.y))
        }
        return res
    }
}
