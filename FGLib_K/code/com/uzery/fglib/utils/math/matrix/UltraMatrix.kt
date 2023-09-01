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
    fun levelFor(pos: PointN, j: Int) = (0 until width).sumOf { i -> pos[j]*data[i, j] }

    fun move(pos: PointN) {
        for (stroke in 0 until height){
            val lv = levelFor(pos, stroke)/level(stroke)
            for (row in 0 until width){
                data[row, stroke] = data[row, stroke]*lv
            }
        }
    }

    fun copy(pos: PointN): UltraMatrix {
        val res = UltraMatrix(data.copy())
        if (pos != PointN.ZERO) res.move(pos)
        return res
    }

    fun into(pos: PointN): Boolean {
        return (0 until height).all { j -> MathUtils.little(levelFor(pos, j)) }
    }

    fun exists(): Boolean{
        val matrix = copy()

        println("!!! start")
        println(matrix)


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

        var lastStroke = 0
        for (row in 0 until width){
            val notZeroRow = findFirstRowAfter(row)
            if(notZeroRow==-1){
                break
            }
            matrix.swapRows(row, notZeroRow)
            matrix.swapStrokes(row, findFirstInRow(row))

            for (stroke in row+1 until height){
                if(!MathUtils.little(matrix.data[row, stroke])){
                    matrix.multiplyStroke(stroke, matrix.data[row, row]/matrix.data[row, stroke])
                    matrix.minusStrokes(stroke, row)
                }
            }
            lastStroke = row
        }
        println("!!! mid")
        println(matrix)
        println("last_stroke: $lastStroke")
        println("height: $height")
        println("!!! finish")
        for(stroke in lastStroke+1 until height){
            if(!MathUtils.little(level(stroke))) return false
        }

        return true
    }
}