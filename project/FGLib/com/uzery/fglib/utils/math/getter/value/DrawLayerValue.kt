package com.uzery.fglib.utils.math.getter.value

import com.uzery.fglib.core.obj.DrawLayer

data class DrawLayerValue(val input: DrawLayer): ObjectValue {
    //todo to PointN
    //todo to ObjectValue
    override fun toString(): String {
        return "layer[${input.name}]"
    }
}