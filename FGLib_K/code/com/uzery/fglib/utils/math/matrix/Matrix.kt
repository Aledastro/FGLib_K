package com.uzery.fglib.utils.math.matrix

import com.uzery.fglib.utils.data.debug.DebugData

open class Matrix(val data: Array2<Double>) {

    val size
        get() = data.size

    val width = data.width
    val height = data.height

    operator fun plus(other: Matrix): Matrix {
        if (this.size != other.size) throw DebugData.error("WRONG PLUS OPERATION: \n$this\n\n$other")

        val res = Array2(size, 0.0)
        res.set { i, j -> this[i, j]+other[i, j] }

        return Matrix(res)
    }
    operator fun minus(other: Matrix): Matrix {
        if (this.size != other.size) throw DebugData.error("WRONG PLUS OPERATION: \n$this\n\n$other")

        val res = Array2(size, 0.0)
        res.set { i, j -> this[i, j]+other[i, j] }

        return Matrix(res)
    }

    operator fun unaryMinus(): Matrix {
        val res = Array2(size, 0.0)
        res.set { i, j -> -this[i, j] }

        return Matrix(res)
    }


    operator fun get(i: Int, j: Int): Double {
        return data[i, j]
    }
}