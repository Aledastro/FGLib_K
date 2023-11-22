package com.uzery.fglib.utils.math.solve

abstract class SolveNumber(val expression: String) {
    abstract operator fun plus(other: SolveNumber): SolveNumber
    abstract operator fun minus(other: SolveNumber): SolveNumber

    abstract operator fun times(other: SolveNumber): SolveNumber

    abstract operator fun div(other: SolveNumber): SolveNumber
}
