package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.num.IntI

abstract class FGImagePackager {
    abstract fun createImage(filename: String): Any
    abstract fun size(source: Any): IntI
    abstract fun colorFrom(source: Any, pos: IntI): FGColor
    abstract fun argbFrom(source: Any, pos: IntI): Int


    abstract fun createWritableImage(size: IntI): Any
    abstract fun setColor(source: Any, pos: IntI, fgColor: FGColor)
    abstract fun setPixels(source: Any, dest: IntI, size: IntI, origin: Any, src: IntI)
    abstract fun setArgb(source: Any, pos: IntI, argb: Int)

    abstract fun writeImage(image: Any, path: String)
}
