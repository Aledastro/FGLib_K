package com.uzery.fglib.utils.math.matrix

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.struct.Array2

/**
 * TODO("doc")
 **/
open class Matrix(val data: Array2<Double>) {
    val size
        get() = data.size

    val width = data.width
    val height = data.height

    operator fun plus(other: Matrix): Matrix {
        if (this.size != other.size) throw DebugData.error("WRONG PLUS OPERATION: \n$this\n\n$other")

        val res = Array2(size) { 0.0 }
        res.set { i, j -> this[i, j]+other[i, j] }

        return Matrix(res)
    }

    operator fun minus(other: Matrix): Matrix {
        if (this.size != other.size) throw DebugData.error("WRONG PLUS OPERATION: \n$this\n\n$other")

        val res = Array2(size) { 0.0 }
        res.set { i, j -> this[i, j]-other[i, j] }

        return Matrix(res)
    }

    open operator fun unaryMinus() = this*-1

    operator fun get(i: Int, j: Int): Double {
        return data[i, j]
    }

    fun copy(): Matrix {
        return Matrix(data.copy())
    }

    fun swapRows(row1: Int, row2: Int) {
        if (row1 == row2) return
        for (i in 0..<height) {
            val temp1 = data[row1, i]
            val temp2 = data[row2, i]
            data[row1, i] = temp2
            data[row2, i] = temp1
        }
    }

    fun swapStrokes(stroke1: Int, stroke2: Int) {
        if (stroke1 == stroke2) return

        for (i in 0..<width) {
            val temp1 = data[i, stroke1]
            val temp2 = data[i, stroke2]
            data[i, stroke1] = temp2
            data[i, stroke2] = temp1
        }
    }

    fun multiplyStroke(stroke: Int, value: Double) {
        for (i in 0..<width) {
            data[i, stroke] = data[i, stroke]*value
        }
    }

    fun minusStrokes(stroke1: Int, stroke2: Int) {
        for (i in 0..<width) {
            data[i, stroke1] -= data[i, stroke2]
        }
    }

    override fun toString(): String {
        return buildString {
            append("\n")
            for (stroke in 0..<height) {
                append("| ")
                for (row in 0..<width) {
                    append("${data[row, stroke]} ")
                }
                append("|\n")
            }
            append("\n")
        }
    }

    open operator fun times(c: Double): Matrix {
        val res = copy()
        res.data.set { i, j -> data[i, j]*c }
        return res
    }

    open operator fun times(c: Int): Matrix {
        val res = copy()
        res.data.set { i, j -> data[i, j]*c }
        return res
    }
}
