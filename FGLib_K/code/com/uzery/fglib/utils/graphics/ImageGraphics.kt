package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.image.Image

abstract class ImageGraphics(private val transform: AffineTransform) {
    fun setDefaults() {

    }

    protected abstract fun draw0(filename: String, pos: PointN, size: PointN)

    protected abstract fun draw0(image: Image, pos: PointN, size: PointN)

    protected abstract fun draw0(filename: String, pos: PointN)

    protected abstract fun draw0(image: Image, pos: PointN)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(filename: String, pos: PointN, size: PointN) =
        draw0(filename, transform.pos(pos), transform.size(pos, size))

    fun drawL(filename: String, pos: PointN, size: PointN) = draw(filename, pos, size)
    fun drawC(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size/2, size)
    fun drawR(filename: String, pos: PointN, size: PointN) = draw(filename, pos-size, size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: Image, pos: PointN, size: PointN) = draw0(image, transform.pos(pos), transform.size(pos, size))

    fun drawL(image: Image, pos: PointN, size: PointN) = draw(image, pos, size)
    fun drawC(image: Image, pos: PointN, size: PointN) = draw(image, pos-size/2, size)
    fun drawR(image: Image, pos: PointN, size: PointN) = draw(image, pos-size, size)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(image: Image, pos: PointN) = draw0(image, transform.pos(pos))

    fun drawL(image: Image, pos: PointN) = draw(image, pos)
    fun drawC(image: Image, pos: PointN) =
        draw0(image, transform.pos(pos)-PointN(image.width, image.height)*Platform.scale/2)

    fun drawR(image: Image, pos: PointN) =
        draw0(image, transform.pos(pos)-PointN(image.width, image.height)*Platform.scale)
}
