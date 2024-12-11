package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.data.image.FGImage
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class ImageGraphics(private val agc: AffineGraphics) {
    protected abstract fun draw0(image: FGImage, pos: PointN, size: PointN, alpha: Double)

    protected val transform
        get() = agc.transform

    fun setDefaults() {

    }

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN, size: PointN) {
        if (agc.isOutOfBounds(pos, size)) return

        draw0(image, transform.pos(pos), transform.size(pos, size), agc.getAlpha())
    }

    ///////////////////////////////////////////////////////////////////////////

    private fun sizeOf(image: FGImage) = PointN(image.size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(filename: String, pos: PointN, size: PointN) {
        ImageData.set(filename)
        draw(ImageData[filename], pos, size)
    }

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN) = draw(image, pos, sizeOf(image))
    fun drawL(image: FGImage, pos: PointN) = drawL(image, pos, sizeOf(image))
    fun drawC(image: FGImage, pos: PointN) = drawC(image, pos, sizeOf(image))
    fun drawR(image: FGImage, pos: PointN) = drawR(image, pos, sizeOf(image))

    fun drawL(image: FGImage, pos: PointN, size: PointN) = draw(image, pos, size)
    fun drawC(image: FGImage, pos: PointN, size: PointN) = draw(image, pos-size/2, size)
    fun drawR(image: FGImage, pos: PointN, size: PointN) = draw(image, pos-size, size)

    fun drawL(filename: String, pos: PointN, size: PointN) = draw(filename, pos, size)
    fun drawC(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size/2, size)
    fun drawR(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size, size)
}
