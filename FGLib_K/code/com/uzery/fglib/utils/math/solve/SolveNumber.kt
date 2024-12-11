package com.uzery.fglib.utils.math.solve

/**
 * TODO("doc")
 **/
abstract class SolveNumber(val expression: String) {
    abstract operator fun plus(other: SolveNumber): SolveNumber
    abstract operator fun minus(other: SolveNumber): SolveNumber

    abstract operator fun times(other: SolveNumber): SolveNumber

    abstract operator fun div(other: SolveNumber): SolveNumber
    abstract operator fun rem(other: SolveNumber): SolveNumber

    open fun divFull(other: SolveNumber): SolveNumber = div(other)

    abstract fun pow(other: SolveNumber): SolveNumber
}
