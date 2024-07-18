package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.data.image.FGImage
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.data.image.ImageUtils
import com.uzery.fglib.utils.math.geom.PointN

abstract class ImageGraphics(private val agc: AffineGraphics) {
    val transform
        get() = agc.transform

    var alpha = 1.0
    fun setDefaults() {
        alpha = 1.0
    }

    protected abstract fun draw0(image: FGImage, pos: PointN, size: PointN)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN, size: PointN) {
        agc.applyAlphaWith(alpha)

        draw0(image, transform.pos(pos), transform.size(pos, size))
    }

    fun drawL(image: FGImage, pos: PointN, size: PointN) = draw(image, pos, size)
    fun drawC(image: FGImage, pos: PointN, size: PointN) = draw(image, pos-size/2, size)
    fun drawR(image: FGImage, pos: PointN, size: PointN) = draw(image, pos-size, size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN) = draw(image, pos, sizeOf(image))

    fun drawL(image: FGImage, pos: PointN) = drawL(image, pos, sizeOf(image))
    fun drawC(image: FGImage, pos: PointN) = drawC(image, pos, sizeOf(image))
    fun drawR(image: FGImage, pos: PointN) = drawR(image, pos, sizeOf(image))

    private fun sizeOf(image: FGImage) = PointN(image.size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(filename: String, pos: PointN, size: PointN) {
        ImageData.set(filename)
        draw(ImageData[filename], pos, size)
    }

    fun drawL(filename: String, pos: PointN, size: PointN) = draw(filename, pos, size)
    fun drawC(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size/2, size)
    fun drawR(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size, size)
}
