package com.uzery.fglib.utils.math.matrix

import com.uzery.fglib.utils.math.num.IntI
import java.util.*

class Array2<Type>(val size: IntI, private val default_value: Type) {
    constructor(width: Int, height: Int, default_value: Type): this(IntI(width, height), default_value)

    val data = LinkedList<Type?>()

    init {
        for (i in 0 until size.width*size.height) data.add(null)
    }

    val width = size.width
    val height = size.height

    private fun from(i: Int, j: Int) = i*height+j
    operator fun get(i: Int, j: Int): Type {
        return data[from(i, j)] ?: default_value
    }

    operator fun set(i: Int, j: Int, value: Type) {
        data[from(i, j)] = value
    }

    fun set(f: (i: Int, j: Int) -> Type) {
        for (i in 0 until width) {
            for (j in 0 until height) {
                set(i, j, f(i, j))
            }
        }
    }

    fun copy(): Array2<Type> {
        val res = Array2(size, default_value)
        res.set { i, j -> get(i, j) }
        return res
    }
}
