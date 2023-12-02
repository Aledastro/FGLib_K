package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform.packager
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

open class WritableFGImage(val size: IntI) {

    fun setColor(pos: IntI, fgColor: FGColor) {
        source.pixelWriter.setColor(pos.x, pos.y, packager.fromFGColor(fgColor) as Color)
    }

    fun getColor(i: Int, j: Int): FGColor {
        return packager.fromColor(source.pixelReader.getColor(i, j).toString())
    }

    fun setPixels(dest: IntI, size: IntI, origin: FGImage, src: IntI) {
        source.pixelWriter.setPixels(dest.x, dest.y, size.width, size.height, origin.source.pixelReader, src.x, src.y)
    }

    fun setArgb(pos: IntI, argb: Int) {
        source.pixelWriter.setArgb(pos.x, pos.y, argb)
    }

    fun getArgb(pos: IntI): Int {
        return source.pixelReader.getArgb(pos.x, pos.y)
    }

    fun draw(origin: FGImage) {
        setPixels(IntI(), origin.size, origin, IntI())
    }

    fun toFGImage(): FGImage {
        return FGImage(source)
    }

    val source = WritableImage(size.width, size.height)
}
