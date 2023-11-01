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
        val img = writableFrom(origin)

        val res_size = scale*IntI(origin.width.toInt(), origin.height.toInt())
        val res = WritableImage(res_size.width, res_size.height)
        for (pos in res_size.indices) {
            res.pixelWriter.setArgb(pos.x, pos.y, img.pixelReader.getArgb(pos.x/scale.x, pos.y/scale.y))
        }
        return res
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun from(path: String): Image {
        return Image("file:"+ImageData.resolvePath(path))
    }

    fun from(path: String, vararg effects: String): Image {
        return from(from(path), *effects)
    }

    fun from(origin: Image, vararg effects: String): Image {
        var res = writableFrom(origin)

        for (effect in effects) {
            res = ImageTransformUtils.applyEffect(res, effect)
        }

        return res
    }

    fun writableFrom(origin: Image): WritableImage{
        val origin_size = IntI(origin.width.toInt(), origin.height.toInt())

        val img = WritableImage(origin_size.width, origin_size.height)
        img.pixelWriter.setPixels(0, 0, origin_size.width, origin_size.height, origin.pixelReader, 0, 0)

        return img
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
