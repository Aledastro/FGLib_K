package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
class SetValue(val value: IntI) {
    override fun toString(): String {
        return "set[${value.x}, ${value.y}]"
    }
}
