package com.uzery.fglib.utils.math.solve

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
}
