package com.uzery.fglib.utils.struct.num

data class StringNN(val s: String, val n: Int, val m: Int): Comparable<StringNN> {
    constructor(s: String): this(s, 0, 0)

    override fun compareTo(other: StringNN): Int {
        val i = s.compareTo(other.s)
        if (i != 0) return i
        val zi = n.compareTo(other.n)
        if (zi != 0) return zi
        return m.compareTo(other.m)
    }
}
