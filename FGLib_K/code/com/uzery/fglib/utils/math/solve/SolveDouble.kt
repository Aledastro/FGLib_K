package com.uzery.fglib.utils.math.solve

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
}
