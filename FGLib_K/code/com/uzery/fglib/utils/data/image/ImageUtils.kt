package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import kotlin.math.min

interface ImageUtils {
    companion object {
        fun combination(
            size: IntI,
            images: Array<Image>,
            positions: Array<IntI> = Array(images.size) { IntI() },
        ): Image {
            val image = WritableImage(size.n, size.m)
            for(i in images.indices) {
                draw(image, images[i], positions[i])
            }
            return image
        }

        private fun draw(origin: WritableImage, image: Image, pos: IntI) {
            /*origin.pixelWriter.setPixels(pos.n, pos.m, min(origin.width-pos.n, image.width).toInt(),
                min(origin.height-pos.m, image.height).toInt(), image.pixelReader, 0, 0)*/
            for(i in 0 until min(origin.width - pos.n, image.width).toInt()) {
                for(j in 0 until min(origin.height - pos.m, image.height).toInt()) {
                    if(image.pixelReader.getColor(i, j) == Color.TRANSPARENT) continue
                    origin.pixelWriter.setArgb(pos.n + i, pos.m + j, image.pixelReader.getArgb(i, j))
                }
            }
        }

        fun split(origin: Image, pos: IntI, size: IntI): Image {
            val res = WritableImage(size.n, size.m)
            res.pixelWriter.setPixels(0, 0, size.n, size.m, origin.pixelReader, pos.n, pos.m)
            return res
        }

        fun scale(origin: Image, scale: IntI): Image {
            val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

            val img = WritableImage(origin_size.n, origin_size.m)
            img.pixelWriter.setPixels(0, 0, origin_size.n, origin_size.m, origin.pixelReader, 0, 0)

            val image_size = scale*origin_size
            val res = WritableImage(image_size.n, image_size.m)
            for(i in 0 until res.width.toInt()) {
                for(j in 0 until res.height.toInt()) {
                    res.pixelWriter.setArgb(i, j, img.pixelReader.getArgb(i/scale.n, j/scale.m))
                }
            }
            return res
        }
    }
}