package com.uzery.fglib.utils.math.solve

import kotlin.math.pow

/**
 * TODO("doc")
 **/
class SolveDouble(val value: Double): SolveNumber(value.toString()) {
    override fun plus(other: SolveNumber): SolveNumber {
        return SolveDouble(this.value+(other as SolveDouble).value)
    }

    override fun minus(other: SolveNumber): SolveNumber {
        return SolveDouble(this.value-(other as SolveDouble).value)
    }

    override fun times(other: SolveNumber): SolveNumber {
        return SolveDouble(this.value*(other as SolveDouble).value)
    }

    override fun div(other: SolveNumber): SolveNumber {
        return SolveDouble(this.value/(other as SolveDouble).value)
    }

    override fun rem(other: SolveNumber): SolveNumber {
        return SolveDouble(this.value%(other as SolveDouble).value)
    }

    override fun divFull(other: SolveNumber): SolveNumber {
        return SolveDouble((this.value.toInt()/(other as SolveDouble).value.toInt()).toDouble())
    }

    override fun pow(other: SolveNumber): SolveNumber {
        return SolveDouble(this.value.pow((other as SolveDouble).value))
    }
}
