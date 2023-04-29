package com.uzery.fglib.utils.math.num

data class IntI(val n: Int, val m: Int): Comparable<IntI> {
    constructor(s: String): this(0, 0)

    override fun compareTo(other: IntI): Int {
        val i = n.compareTo(other.n)
        if(i != 0) return i
        return m.compareTo(other.m)
    }

    operator fun times(other: IntI): IntI {
        return IntI(n*other.n, m*other.m)
    }

    operator fun div(other: IntI): IntI {
        return IntI(n/other.n, m/other.m)
    }

    operator fun plus(other: IntI): IntI {
        return IntI(n + other.n, m + other.m)
    }

    operator fun minus(other: IntI): IntI {
        return IntI(n - other.n, m - other.m)
    }
}
