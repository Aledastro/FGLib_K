package com.uzery.fglib.core.realisation.packager

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.struct.num.IntI

abstract class FGImagePackager {
    abstract fun createImage(filename: String): Any
    abstract fun size(source: Any): IntI
    abstract fun argbFrom(source: Any, pos: IntI): Int

    fun colorFrom(source: Any, pos: IntI): FGColor {
        return FGColor.argb(argbFrom(source, pos))
    }

    abstract fun createWritableImage(size: IntI): Any
    abstract fun setPixels(source: Any, pos: IntI, size: IntI, origin: Any)
    abstract fun setArgb(source: Any, pos: IntI, argb: Int)

    fun setColor(source: Any, pos: IntI, fgColor: FGColor) {
        setArgb(source, pos, fgColor.argb)
    }

    abstract fun writeImage(source: Any, path: String)
}
