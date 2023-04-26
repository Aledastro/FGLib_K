package com.uzery.fglib.utils.math.num

data class StringDD(val s: String, val d1: Double, val d2: Double): Comparable<StringDD> {
    constructor(s: String): this(s, 0.0, 0.0)

    override fun compareTo(other: StringDD): Int {
        val i = s.compareTo(other.s)
        if(i != 0) return i
        val zi = d1.compareTo(other.d1)
        if(zi != 0) return zi
        return d2.compareTo(other.d2)
    }
}
