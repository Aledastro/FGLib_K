package com.uzery.fglib.utils.math.matrix

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.abs

class UltraMatrix(data: Array2<Double>): Matrix(data) {
    constructor(vararg normal: PointN): this(MatrixUtils.toArray(*normal))

    private val strokes_values = Array(height) { level(it) }

    val sign = Array(height) { 1 }

    fun connect(other: UltraMatrix): UltraMatrix {
        if (this.width != other.width) throw DebugData.error("WRONG CONNECT OPERATION: \n$this\n\n$other")
        val res = Array2(width, height+other.height, 0.0)
        res.set { i, j -> if (j < height) this[i, j] else other[i, j-height] }
        return UltraMatrix(res)
    }

    private fun level(j: Int) = (0 until width).sumOf { i -> data[i, j]*data[i, j] }
    private fun levelFor(pos: PointN, j: Int) = (0 until width).sumOf { i -> pos[i]*data[i, j] }

    fun move(pos: PointN) {
        for (stroke in 0 until height) {
            val xs = Array(width) { data[it, stroke] }
            val c_pos = PointN(xs)
            val lv = levelFor(c_pos+pos, stroke)/levelFor(c_pos, stroke)
            if (lv < 0) sign[stroke] *= -1

            for (row in 0 until width) {
                data[row, stroke] *= lv
            }
        }
    }

    fun copy(pos: PointN): UltraMatrix {
        val res = copyU()
        if (pos != PointN.ZERO) res.move(pos)
        return res
    }

    fun copyU(): UltraMatrix {
        return UltraMatrix(data.copy())
    }


    fun into(pos: PointN): Boolean {
        return (0 until height).all { j -> MathUtils.little(levelFor(pos, j)-level(j)) }
    }

    fun intoS(pos: PointN): Boolean {
        return (0 until height).all { j -> abs(levelFor(pos, j)-level(j)) < 10 }
    }

    fun intoHalf(pos: PointN): Boolean {
        return (0 until height).all { j -> (levelFor(pos, j)-level(j))*sign[j] <= 0 }
    }

    fun intoHalfS(pos: PointN, value: Double = 1.0): Boolean {
        return (0 until height).all { j -> (levelFor(pos, j)-level(j))*sign[j] <= value }
    }

    private val rows_panel = Array(width) { it }

    private fun swapStrokesX(s1: Int, s2: Int) {
        swapStrokes(s1, s2)

        val sav1 = strokes_values[s1]
        val sav2 = strokes_values[s2]
        strokes_values[s1] = sav2
        strokes_values[s2] = sav1
    }

    private fun swapRowsX(r1: Int, r2: Int) {
        swapRows(r1, r2)

        val sav1 = rows_panel[r1]
        val sav2 = rows_panel[r2]
        rows_panel[r1] = sav2
        rows_panel[r2] = sav1
    }

    private fun multiplyStrokeX(s1: Int, v: Double) {
        multiplyStroke(s1, v)
        strokes_values[s1] *= v
    }

    private fun minusStrokesX(s1: Int, s2: Int) {
        minusStrokes(s1, s2)
        strokes_values[s1] -= strokes_values[s2]
    }

    fun toTriangle(): Int {
        fun findFirstInRow(row: Int): Int {
            for (j in row until height) {
                if (data[row, j] != 0.0) return j
            }
            return -1
        }

        fun findFirstRowAfter(row: Int): Int {
            for (i in row until width) {
                for (j in row until height) {
                    if (data[i, j] != 0.0) return i
                }
            }
            return -1
        }

        var lastStroke = 0
        for (row in 0 until width) {
            val notZeroRow = findFirstRowAfter(row)
            if (notZeroRow == -1) {
                break
            }
            swapRowsX(row, notZeroRow)
            swapStrokesX(row, findFirstInRow(row))

            for (stroke in row+1 until height) {
                if ((data[row, stroke]) != 0.0) {
                    multiplyStrokeX(stroke, data[row, row]/data[row, stroke])
                    minusStrokesX(stroke, row)
                }
            }
            lastStroke = row
        }

        return lastStroke
    }

    private fun toDiagonal(): Boolean {
        val lastStroke = toTriangle()

        if (lastStroke != height-1 || width != height) return false

        for (stroke in lastStroke+1 until height) {
            if (!MathUtils.little(strokes_values[stroke])) return false
        }

        for (row in 0 until width) {
            val actual_row = width-1-row

            for (stroke in 0 until actual_row) {
                if ((data[actual_row, stroke]) != 0.0) {
                    multiplyStrokeX(stroke, data[actual_row, actual_row]/data[actual_row, stroke])
                    minusStrokesX(stroke, actual_row)
                }
            }
        }
        return true
    }

    fun exists(): Boolean {
        val lastStroke = copyU().toTriangle()

        for (stroke in lastStroke+1 until height) {
            if (!MathUtils.little(level(stroke))) return false
        }

        return true
    }

    override operator fun unaryMinus(): UltraMatrix {
        return this*-1
    }

    override operator fun times(c: Double): UltraMatrix {
        val res = copyU()
        res.data.set { i, j -> data[i, j]*c }
        return res
    }

    override operator fun times(c: Int): UltraMatrix {
        val res = copyU()
        res.data.set { i, j -> data[i, j]*c }
        return res
    }

    fun solve(): Array<Double>? {
        val matrix = copyU()

        val successful = matrix.toDiagonal()
        if (!successful) return null


        val xs = Array(matrix.width) { 0.0 }
        for (i in 0 until matrix.width) {
            val id = matrix.rows_panel[i]
            xs[id] = matrix.strokes_values[i]/matrix[i, i]
        }

        return xs
    }
}