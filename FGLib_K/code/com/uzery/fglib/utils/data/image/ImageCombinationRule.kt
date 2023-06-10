package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.math.num.IntI

abstract class ImageCombinationRule(val size: IntI) {
    abstract val count: IntI
    abstract fun from(pos: IntI): List<IntI>

    open fun positions(pos: IntI): Array<IntI>? = null
}
