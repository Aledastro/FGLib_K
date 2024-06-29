package com.uzery.fglib.utils.struct.num

data class StringD(val s: String, val d: Double) {
    constructor(s: String): this(s, 0.0)
}
