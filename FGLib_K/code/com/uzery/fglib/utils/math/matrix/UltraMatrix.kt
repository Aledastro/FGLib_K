package com.uzery.fglib.utils.math.matrix

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.ConstL.Companion.LITTLE
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.abs

class UltraMatrix(data: Array2<Double>): Matrix(data) {
    constructor(vararg normal: PointN): this(MatrixUtils.toArray(*normal))

    fun connect(other: UltraMatrix): UltraMatrix {
        if (this.width != other.width) throw DebugData.error("WRONG CONNECT OPERATION: \n$this\n\n$other")
        val res = Array2(width, height+other.height, 0.0)
        res.set { i, j -> if (j < height) this[i, j] else other[i, j-height] }
        return UltraMatrix(res)
    }

    fun level(j: Int) = (0 until width).sumOf { i -> data[i, j]*data[i, j] }
    fun levelFor(pos: PointN, j: Int) = (0 until width).sumOf { i -> pos[i]*data[i, j] }

    fun move(pos: PointN) {
        data.set { i, j -> data[i, j]*levelFor(pos, j)/level(j) }
    }

    fun copy(pos: PointN = PointN.ZERO): UltraMatrix {
        val res = UltraMatrix(data.copy())
        if (pos != PointN.ZERO) res.move(pos)
        return res
    }

    fun into(pos: PointN): Boolean {
        return (0 until height).all { j -> abs(levelFor(pos, j)) < LITTLE }
    }

    fun exists(): Boolean{
        val matrix = copy()

        fun findFirstInRow(row: Int): Int {
            for (j in row until height){
                if(matrix.data[row, j]!=0.0)return j
            }
            return -1
        }
        fun findFirstRowAfter(row: Int): Int {
            for (i in row until width){
                for (j in row until height){
                    if(matrix.data[i, j]!=0.0)return i
                }
            }
            return -1
        }

        fun swapRows(row1: Int,row2: Int){
            if(row1==row2) return
            val temp = matrix.copy()
            for(i in 0 until height){
                matrix.data[row1, i] = temp[row2, i]
                matrix.data[row2, i] = temp[row1, i]
            }
        }
        fun swapStrokes(stroke1: Int,stroke2: Int){
            if(stroke1==stroke2) return
            val temp = matrix.copy()
            for(i in 0 until width){
                matrix.data[i, stroke1] = temp[i, stroke2]
                matrix.data[i, stroke2] = temp[i, stroke1]
            }
        }
        fun multiplyStroke(stroke: Int, value: Double){
            for(i in 0 until width){
                matrix.data[i, stroke] = matrix.data[i, stroke]*value
            }
        }

        fun minusStrokes(stroke1: Int, stroke2: Int) {
            for(i in 0 until width){
                matrix.data[i, stroke1] -= matrix.data[i, stroke2]
            }
        }

        var lastStroke = 0
        for (row in 0 until width){
            val notZeroRow = findFirstRowAfter(row)
            if(notZeroRow==-1){
                break
            }
            swapRows(row, notZeroRow)
            swapStrokes(row, findFirstInRow(row))

            for (stroke in row+1 until height){
                if(MathUtils.little(matrix.data[row, stroke])){
                    multiplyStroke(stroke, matrix.data[row, row]/matrix.data[row, stroke])
                    minusStrokes(stroke, row)
                }
            }
            lastStroke = row
        }
        for(stroke in lastStroke until height){
            if(!MathUtils.little(level(stroke))) return false
        }

        return true
    }
}