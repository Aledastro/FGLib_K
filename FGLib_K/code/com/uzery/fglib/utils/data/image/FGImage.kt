package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

open class FGImage(val source: Image) {
    fun getColor(pos: IntI): FGColor {
        return Platform.packager.fromColor(source.pixelReader.getColor(pos.x, pos.y).toString())
    }

    fun getArgb(pos: IntI): Int {
        return source.pixelReader.getArgb(pos.x, pos.y)
    }

    val name: String = "temp"
    val size: IntI
        get() = IntI(source.width.toInt(), source.height.toInt())
}
