package com.uzery.fglib.utils.math.num

data class IntInt(val n: Int, val m: Int): Comparable<IntInt> {
    constructor(s: String): this(0, 0)

    override fun compareTo(other: IntInt): Int {
        val i = n.compareTo(other.n)
        if(i != 0) return i
        return m.compareTo(other.m)
    }

    operator fun times(other: IntInt): IntInt {
        return IntInt(n*other.n, m*other.m)
    }

    operator fun div(other: IntInt): IntInt {
        return IntInt(n/other.n, m/other.m)
    }

    operator fun plus(other: IntInt): IntInt {
        return IntInt(n + other.n, m + other.m)
    }

    operator fun minus(other: IntInt): IntInt {
        return IntInt(n - other.n, m - other.m)
    }
}
