package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

abstract class FGLImage {
    val data = HashMap<IntI, Image>()
    lateinit var name: String
    fun get(pos: IntI): Image {
        return data[pos] ?: throw DebugData.error("illegal get from $name $pos")
    }

    abstract fun from(pos: IntI): Image

    protected fun setData(name: String, count: IntI) {
        this.name = name
        for (i in count.indices) {
            data[i] = from(i)
        }
    }
}
