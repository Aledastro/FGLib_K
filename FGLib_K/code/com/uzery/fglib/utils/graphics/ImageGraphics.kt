package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.data.image.FGImage
import com.uzery.fglib.utils.data.image.ImageUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.image.Image

abstract class ImageGraphics(private val transform: AffineTransform) {
    fun setDefaults() {

    }

    protected abstract fun draw0(image: FGImage, pos: PointN, size: PointN)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(filename: String, pos: PointN, size: PointN) =
        draw0(ImageUtils.from(filename), transform.pos(pos), transform.size(pos, size))

    fun drawL(filename: String, pos: PointN, size: PointN) = draw(filename, pos, size)
    fun drawC(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size/2, size)
    fun drawR(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size, size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN, size: PointN) = draw0(image, transform.pos(pos), transform.size(pos, size))

    fun drawL(image: FGImage, pos: PointN, size: PointN) = draw(image, pos, size)
    fun drawC(image: FGImage, pos: PointN, size: PointN) = draw(image, pos-size/2, size)
    fun drawR(image: FGImage, pos: PointN, size: PointN) = draw(image, pos-size, size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN) =
        draw0(image, transform.pos(pos), transform.size(pos, sizeOf(image)))

    fun drawL(image: FGImage, pos: PointN) = draw(image, pos)
    fun drawC(image: FGImage, pos: PointN) =
        draw0(image, transform.pos(pos-sizeOf(image)/2), transform.size(pos, sizeOf(image)))

    fun drawR(image: FGImage, pos: PointN) =
        draw0(image, transform.pos(pos-sizeOf(image)), transform.size(pos, sizeOf(image)))

    private fun sizeOf(image: FGImage) = PointN(image.size)
}
