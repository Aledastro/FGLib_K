package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform.packager
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.num.IntI

open class WritableFGImage(val size: IntI) {
    val source = packager.image.createWritableImage(size)

    fun setColor(pos: IntI, fgColor: FGColor) {
        packager.image.setColor(source, pos, fgColor)
    }

    fun getColor(pos: IntI): FGColor {
        return packager.image.colorFrom(source, pos)
    }

    fun setPixels(dest: IntI, size: IntI, origin: FGImage, src: IntI) {
        packager.image.setPixels(source, dest, size, origin.source, src)
    }

    fun setArgb(pos: IntI, argb: Int) {
        packager.image.setArgb(pos, argb)
    }

    fun getArgb(pos: IntI): Int {
        return packager.image.argbFrom(source, pos)
    }

    fun draw(origin: FGImage) {
        setPixels(IntI(), origin.size, origin, IntI())
    }

    fun toFGImage(): FGImage {
        return FGImage(source)
    }
}
