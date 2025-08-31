package com.uzery.fglib.utils.graphics.sub

import com.uzery.fglib.utils.data.image.FGImage
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class ImageGraphics(agc: AffineGraphics): SubGraphics(agc) {
    protected abstract fun renderImage(image: FGImage, pos: PointN, size: PointN, alpha: Double)

    fun setDefaults() {

    }

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN, size: PointN, layout: PointN = OF_L) {
        renderIn(pos, size, layout) { real_pos, real_size ->
            renderImage(image, real_pos, real_size, agc.getAlpha())
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    private fun sizeOf(image: FGImage) = PointN(image.size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: FGImage, pos: PointN) = draw(image, pos, size = sizeOf(image))
    fun drawL(image: FGImage, pos: PointN) = draw(image, pos, size = sizeOf(image), layout = OF_L)
    fun drawC(image: FGImage, pos: PointN) = draw(image, pos, size = sizeOf(image), layout = OF_C)
    fun drawR(image: FGImage, pos: PointN) = draw(image, pos, size = sizeOf(image), layout = OF_R)

    fun drawL(image: FGImage, pos: PointN, size: PointN) = draw(image, pos, size, layout = OF_L)
    fun drawC(image: FGImage, pos: PointN, size: PointN) = draw(image, pos, size, layout = OF_C)
    fun drawR(image: FGImage, pos: PointN, size: PointN) = draw(image, pos, size, layout = OF_R)
}
