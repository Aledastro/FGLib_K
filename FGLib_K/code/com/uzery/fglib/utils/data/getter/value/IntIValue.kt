package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
class IntIValue(val value: IntI): ObjectValue {
    override fun toString(): String {
        return "[${value.width}, ${value.height}]"
    }
}
