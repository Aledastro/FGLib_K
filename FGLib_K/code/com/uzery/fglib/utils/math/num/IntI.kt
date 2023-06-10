package com.uzery.fglib.utils.math.num

data class IntI(val n: Int, val m: Int) {
    constructor(): this(0, 0)

    val indices
        get() = Array(n*m) { i -> IntI(i%n, i/n) }

    operator fun times(other: IntI) = IntI(n*other.n, m*other.m)

    operator fun div(other: IntI) = IntI(n/other.n, m/other.m)

    operator fun plus(other: IntI) = IntI(n + other.n, m + other.m)

    operator fun minus(other: IntI) = IntI(n - other.n, m - other.m)

    operator fun times(other: Int) = IntI(n*other, m*other)
    operator fun div(other: Int) = IntI(n/other, m/other)

}
