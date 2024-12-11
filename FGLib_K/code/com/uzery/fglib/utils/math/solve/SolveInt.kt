package com.uzery.fglib.utils.math.solve

import kotlin.math.pow

/**
 * TODO("doc")
 **/
class SolveInt(val value: Int): SolveNumber(value.toString()) {
    override fun plus(other: SolveNumber): SolveNumber {
        return SolveInt(this.value+(other as SolveInt).value)
    }

    override fun minus(other: SolveNumber): SolveNumber {
        return SolveInt(this.value-(other as SolveInt).value)
    }

    override fun times(other: SolveNumber): SolveNumber {
        return SolveInt(this.value*(other as SolveInt).value)
    }

    override fun div(other: SolveNumber): SolveNumber {
        return SolveInt(this.value/(other as SolveInt).value)
    }

    override fun rem(other: SolveNumber): SolveNumber {
        return SolveInt(this.value%(other as SolveInt).value)
    }

    override fun pow(other: SolveNumber): SolveNumber {
        return SolveInt(this.value.toDouble().pow((other as SolveInt).value.toDouble()).toInt())
    }
}
