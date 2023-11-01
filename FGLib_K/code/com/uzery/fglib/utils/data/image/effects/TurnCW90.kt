package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.WritableImage

object TurnCW90: ImageTransformEffect("turnCW90") {
    override operator fun get(origin: WritableImage, args: List<List<String>>): WritableImage {
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

        val res = WritableImage(origin_size.height, origin_size.width)
        for (pos in origin_size.indices) {
            res.pixelWriter.setArgb(pos.x, pos.y, origin.pixelReader.getArgb(origin_size.height-1-pos.y, pos.x))
        }
        return res
    }
}
