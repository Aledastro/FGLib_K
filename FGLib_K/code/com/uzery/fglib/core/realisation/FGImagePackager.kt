package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.struct.num.IntI

abstract class FGImagePackager {
    abstract fun createImage(filename: String): Any
    abstract fun size(source: Any): IntI
    abstract fun colorFrom(source: Any, pos: IntI): FGColor //todo from argbFrom()
    abstract fun argbFrom(source: Any, pos: IntI): Int


    abstract fun createWritableImage(size: IntI): Any
    abstract fun setColor(source: Any, pos: IntI, fgColor: FGColor) //todo from setArgb()
    abstract fun setPixels(source: Any, pos: IntI, size: IntI, origin: Any)
    abstract fun setArgb(source: Any, pos: IntI, argb: Int)

    abstract fun writeImage(source: Any, path: String)
}
