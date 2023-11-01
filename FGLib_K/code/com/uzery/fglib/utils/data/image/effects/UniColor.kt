package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

object UniColor: ImageTransformEffect("uni_color") {
    override operator fun get(origin: WritableImage, args: List<List<String>>): WritableImage {
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())
        val color = Color.web(args[0][0])

        val res = WritableImage(origin_size.width, origin_size.height)
        for (pos in origin_size.indices) {
            val rgb = origin.pixelReader.getColor(pos.x, pos.y)
            res.pixelWriter.setColor(pos.x, pos.y, if (rgb == Color.TRANSPARENT) rgb else color)
        }
        return res
    }
}
