package com.uzery.fglib.utils

import com.uzery.fglib.utils.math.geom.FieldN
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.Shape.Code
import com.uzery.fglib.utils.math.geom.shape.FigureN
import com.uzery.fglib.utils.math.geom.shape.FigureRectN
import com.uzery.fglib.utils.math.geom.shape.OvalN
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.max

/**
 * TODO("doc")
 **/
object ShapeUtils {
    fun rect(shape: Shape) = RectN(shape.L, shape.S)
    fun oval(shape: Shape) = OvalN(shape.L, shape.S)

    private fun intoRect(first: RectN, second: RectN): Boolean {
        return (0..<first.dim).all { i ->
            first.L[i] < second.R[i] && second.L[i] < first.R[i]
        }
    }

    private fun intoOval(first: OvalN, second: OvalN): Boolean {
        var max_r1 = 0.0
        var max_r2 = 0.0

        for (i in 0..<first.C.dim) {
            max_r1 = max(max_r1, first.S[i])
            max_r2 = max(max_r2, second.S[i])
        }
        if ((first.C-second.C).length() < (max_r1+max_r2)/2) return false

        //todo circleN
        return (first.C-second.C).length() < (first.S[0]+second.S[0])/2
    }

    private fun intoFigure(first: FigureN, second: FigureN): Boolean {
        return (first*second).exists()
    }

    private fun intoRectOval(rect: RectN, oval: OvalN): Boolean {
        return intoOvalFigure(oval, FigureRectN(rect))
    }

    private fun intoRectFigure(rect: RectN, figure: FigureN): Boolean {
        return intoFigure(FigureRectN(rect), figure)
    }

    private fun intoOvalFigure(oval: OvalN, figure: FigureN): Boolean {
        return figure.fields.all { f ->
            f.intoHalf(oval.C, oval.S[0]/2) //todo
        }
    }

    fun into(first: Shape, second: Shape): Boolean {
        if (first is RectN && second is RectN) return intoRect(first, second)

        if (!intoRect(rect(first), rect(second))) return false

        return when (first.code) {
            Code.RECT -> {
                first as RectN
                when (second.code) {
                    Code.RECT -> true
                    Code.OVAL -> intoRectOval(first, second as OvalN)
                    Code.FIGURE -> intoRectFigure(first, second as FigureN)
                }
            }

            Code.OVAL -> {
                first as OvalN
                when (second.code) {
                    Code.RECT -> intoRectOval(second as RectN, first)
                    Code.OVAL -> intoOval(first, second as OvalN)
                    Code.FIGURE -> intoOvalFigure(first, second as FigureN)
                }
            }

            Code.FIGURE -> {
                first as FigureN
                when (second.code) {
                    Code.RECT -> intoRectFigure(second as RectN, first)
                    Code.OVAL -> intoOvalFigure(second as OvalN, first)
                    Code.FIGURE -> intoFigure(first, second as FigureN)
                }
            }
        }
    }

    fun interpolate(start: RectN, finish: RectN, k: Double): RectN {
        return RectN.LR(start.L.interpolate(finish.L, k), start.R.interpolate(finish.R, k))
    }

    fun mainOf(vararg list: Shape): RectN {
        val minP = PointN(Array(list[0].dim) { i -> list.minOf { p -> p.L[i] } })
        val maxP = PointN(Array(list[0].dim) { i -> list.maxOf { p -> p.R[i] } })

        return RectN.LR(minP, maxP)
    }

    fun getFields(pos: PointN, size: PointN): List<FieldN> {
        val fields = ArrayList<FieldN>()
        val dim = size.dim

        for (level in 0..<dim) {
            val xs = Array(dim) { i -> if (i == level) size[i]/2 else 0.0 }
            val field = FieldN(PointN.ZERO, PointN(xs))
            fields.add(field.copy(pos))
            fields.add((-field).copy(pos))
        }

        return fields
    }
}
