package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.WriteData.resolvePath
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
        for (i in 0 until min(origin.width-pos.x, image.width).toInt()) {
            for (j in 0 until min(origin.height-pos.y, image.height).toInt()) {
                if (image.pixelReader.getColor(i, j) == Color.TRANSPARENT) continue
                origin.pixelWriter.setArgb(pos.x+i, pos.y+j, image.pixelReader.getArgb(i, j))
            }
        }
    }

    fun split(origin: Image, pos: IntI, size: IntI): Image {
        val res = WritableImage(size.width, size.height)
        res.pixelWriter.setPixels(0, 0, size.width, size.height, origin.pixelReader, pos.x, pos.y)
        return res
    }

    fun scale(origin: Image, scale: IntI): Image {
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

        val img = WritableImage(origin_size.width, origin_size.height)
        img.pixelWriter.setPixels(0, 0, origin_size.width, origin_size.height, origin.pixelReader, 0, 0)

        val image_size = scale*origin_size
        val res = WritableImage(image_size.width, image_size.height)
        for (pos in image_size.indices) {
            res.pixelWriter.setArgb(pos.x, pos.y, img.pixelReader.getArgb(pos.x/scale.x, pos.y/scale.y))
        }
        return res
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun from(path: String, vararg effects: String): Image {
        return from(Image(ImageData.resolvePath(path)), *effects)
    }
    fun from(image: Image, vararg effects: String): Image {
        var res = image
        for (effect in effects) {
            res = applyEffect(res, effect)
        }

        return res
    }

    fun reverseX(origin: Image): Image {
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

        val img = WritableImage(origin_size.width, origin_size.height)
        img.pixelWriter.setPixels(0, 0, origin_size.width, origin_size.height, origin.pixelReader, 0, 0)

        val res = WritableImage(origin_size.width, origin_size.height)
        for (pos in origin_size.indices) {
            res.pixelWriter.setArgb(pos.x, pos.y, img.pixelReader.getArgb(origin_size.width-1-pos.x, pos.y))
        }
        return res
    }

    fun reverseY(origin: Image): Image {
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

        val img = WritableImage(origin_size.width, origin_size.height)
        img.pixelWriter.setPixels(0, 0, origin_size.width, origin_size.height, origin.pixelReader, 0, 0)

        val res = WritableImage(origin_size.width, origin_size.height)
        for (pos in origin_size.indices) {
            res.pixelWriter.setArgb(pos.x, pos.y, img.pixelReader.getArgb(pos.x, origin_size.height-1-pos.y))
        }
        return res
    }

    private fun applyEffect(img: Image, effect: String): Image {
        return when (effect) {
            "reverseX" -> reverseX(img)
            "reverseY" -> reverseY(img)

            else -> throw DebugData.error("unknown image effect: $effect")
        }
    }
}
