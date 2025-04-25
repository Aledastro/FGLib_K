package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.core.obj.DrawLayer

/**
 * TODO("doc")
 **/
data class DrawLayerValue(val input: DrawLayer): ObjectValue {
    override fun toString(): String {
        return "layer[${input.name_with_suf}]"
    }
}
