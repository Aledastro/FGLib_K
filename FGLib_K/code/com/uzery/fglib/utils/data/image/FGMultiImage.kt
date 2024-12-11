package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
abstract class FGMultiImage(val origin: FGImage): FGImage(origin.source) {
    val data = HashMap<IntI, FGImage>()
    protected var info = ""
    fun get(pos: IntI): FGImage {
        return data[pos] ?: throw DebugData.error("illegal get from ${origin.name} $pos $info")
    }

    abstract fun from(pos: IntI): FGImage

    protected fun setData(size: IntI) {
        for (pos in size.indices) {
            data[pos] = from(pos)
        }
    }
}
