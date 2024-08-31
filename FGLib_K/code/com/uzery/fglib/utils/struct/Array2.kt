package com.uzery.fglib.utils.struct

import com.uzery.fglib.utils.struct.num.IntI

class Array2<Type>(val size: IntI, private val default_value: () -> Type) {
    constructor(width: Int, height: Int, default_value: () -> Type): this(IntI(width, height), default_value)

    val data = ArrayList<Type>()

    init {
        for (i in 0..<size.width*size.height) data.add(default_value())
    }

    val width = size.width
    val height = size.height

    private fun from(i: Int, j: Int) = i*height+j
    operator fun get(i: Int, j: Int): Type {
        return get(IntI(i, j))
    }

    operator fun get(i: IntI): Type {
        return data[from(i.x, i.y)]
    }

    operator fun set(i: Int, j: Int, value: Type) {
        set(IntI(i, j), value)
    }

    operator fun set(i: IntI, value: Type) {
        data[from(i.x, i.y)] = value
    }

    fun set(f: (i: Int, j: Int) -> Type) {
        for (id in size.indices) {
            set(id, f(id.x, id.y))
        }
    }

    fun copy(): Array2<Type> {
        return Array2(size, default_value).also {
            it.set { i, j -> get(i, j) }
        }
    }
}
