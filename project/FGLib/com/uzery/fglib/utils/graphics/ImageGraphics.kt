package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.image.Image

abstract class ImageGraphics(private val transform: AffineTransform) {
    protected abstract fun draw0(filename: String, pos: PointN, size: PointN)

    protected abstract fun draw0(image: Image, pos: PointN, size: PointN)

    protected abstract fun draw0(filename: String, pos: PointN)

    protected abstract fun draw0(image: Image, pos: PointN)

    //transform.pos(pos),transform.size(pos,size)

    fun draw(filename: String, pos: PointN, size: PointN) {
        draw0(filename, transform.pos(pos), transform.size(pos, size))
    }

    fun draw(image: Image, pos: PointN, size: PointN) {
        draw0(image, transform.pos(pos), transform.size(pos, size))
    }

    fun draw(filename: String, pos: PointN) {
        draw0(filename, transform.pos(pos))
    }

    fun draw(image: Image, pos: PointN) {
        draw0(image, transform.pos(pos))
    }
}
