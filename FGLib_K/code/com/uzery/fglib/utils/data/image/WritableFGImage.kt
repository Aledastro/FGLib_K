package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform.packager
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
open class WritableFGImage(val size: IntI) {
    val source = packager.image.createWritableImage(size)

    fun setColor(pos: IntI, fgColor: FGColor) {
        packager.image.setColor(source, pos, fgColor)
    }

    fun getColor(pos: IntI): FGColor {
        return packager.image.colorFrom(source, pos)
    }

    fun setPixels(pos: IntI, size: IntI, origin: FGImage) {
        packager.image.setPixels(source, pos, size, origin.source)
    }

    fun setArgb(pos: IntI, argb: Int) {
        packager.image.setArgb(source, pos, argb)
    }

    fun getArgb(pos: IntI): Int {
        return packager.image.argbFrom(source, pos)
    }

    fun draw(origin: FGImage) {
        setPixels(IntI(), origin.size, origin)
    }

    fun toFGImage(): FGImage {
        return FGImage(source)
    }
}
