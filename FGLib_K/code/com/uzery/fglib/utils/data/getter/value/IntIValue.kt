package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.utils.math.num.IntI

class IntIValue(val value: IntI): ObjectValue {
    override fun toString(): String {
        return "[${value.width}, ${value.height}]"
    }
}
