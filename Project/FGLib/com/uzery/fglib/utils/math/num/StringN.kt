package com.uzery.fglib.utils.math.num

data class StringN(val s: String, val n: Int): Comparable<StringN> {
    constructor(s: String): this(s, 0)

    override fun compareTo(other: StringN): Int {
        val i = s.compareTo(other.s)
        if(i != 0) return i
        return n.compareTo(other.n)
    }
}
