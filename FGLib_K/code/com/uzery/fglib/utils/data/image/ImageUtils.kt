package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import kotlin.math.min

object ImageUtils {
    fun combination(
        size: IntI,
        images: Array<Image>,
        positions: Array<IntI> = Array(images.size) { IntI() },
    ): Image {
        val image = WritableImage(size.width, size.height)
        for (i in images.indices) {
            draw(image, images[i], positions[i])
        }
        return image
    }

    private fun draw(origin: WritableImage, image: Image, pos: IntI) {
        /*origin.pixelWriter.setPixels(pos.n, pos.m, min(origin.width-pos.n, image.width).toInt(),
            min(origin.height-pos.m, image.height).toInt(), image.pixelReader, 0, 0)*/
        for (i in 0 until min(origin.width-pos.width, image.width).toInt()) {
            for (j in 0 until min(origin.height-pos.height, image.height).toInt()) {
                if (image.pixelReader.getColor(i, j) == Color.TRANSPARENT) continue
                origin.pixelWriter.setArgb(pos.width+i, pos.height+j, image.pixelReader.getArgb(i, j))
            }
        }
    }

    fun split(origin: Image, pos: IntI, size: IntI): Image {
        val res = WritableImage(size.width, size.height)
        res.pixelWriter.setPixels(0, 0, size.width, size.height, origin.pixelReader, pos.width, pos.height)
        return res
    }

    fun scale(origin: Image, scale: IntI): Image {
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

        val img = WritableImage(origin_size.width, origin_size.height)
        img.pixelWriter.setPixels(0, 0, origin_size.width, origin_size.height, origin.pixelReader, 0, 0)

        val image_size = scale*origin_size
        val res = WritableImage(image_size.width, image_size.height)
        for (i in 0 until res.width.toInt()) {
            for (j in 0 until res.height.toInt()) {
                res.pixelWriter.setArgb(i, j, img.pixelReader.getArgb(i/scale.width, j/scale.height))
            }
        }
        return res
    }
}