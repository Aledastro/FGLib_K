package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform.packager
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.struct.num.IntI

open class FGImage(val source: Any) {
    constructor(filename: String): this(packager.image.createImage(filename))

    fun getColor(pos: IntI): FGColor {
        return packager.image.colorFrom(source, pos)
    }

    fun getArgb(pos: IntI): Int {
        return packager.image.argbFrom(source, pos)
    }

    val name: String = "temp"
    val size: IntI
        get() = packager.image.size(source)
}
