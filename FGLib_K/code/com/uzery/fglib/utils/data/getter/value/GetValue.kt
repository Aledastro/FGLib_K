package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
class GetValue(val value: IntI) {
    override fun toString(): String {
        return "get[${value.x}, ${value.y}]"
    }
}
