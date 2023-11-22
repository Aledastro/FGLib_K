package com.uzery.fglib.utils.math.solve

class SolveLong(val value: Long): SolveNumber(value.toString()) {
    override fun plus(other: SolveNumber): SolveNumber {
        return SolveLong(this.value+(other as SolveLong).value)
    }

    override fun minus(other: SolveNumber): SolveNumber {
        return SolveLong(this.value-(other as SolveLong).value)
    }

    override fun times(other: SolveNumber): SolveNumber {
        return SolveLong(this.value*(other as SolveLong).value)
    }

    override fun div(other: SolveNumber): SolveNumber {
        return SolveLong(this.value/(other as SolveLong).value)
    }
}
