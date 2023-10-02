package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.FigureN
import com.uzery.fglib.utils.math.geom.shape.FigureRectN
import com.uzery.fglib.utils.math.geom.shape.OvalN
import com.uzery.fglib.utils.math.geom.shape.RectN

object ShapeUtils {
    fun rect(shape: Shape) = RectN(shape.L, shape.S)
    fun oval(shape: Shape) = OvalN(shape.C, shape.S)

    fun intoRect(first: RectN, second: RectN): Boolean {
        return (0 until first.dim).all { i -> first.L[i] < second.R[i] && second.L[i] < first.R[i] }
    }

    private fun intoOval(first: OvalN, second: OvalN): Boolean {
        //todo circleN
        return (first.C-second.C).length() < (first.S[0]+second.S[0])/2
    }

    private fun intoFigure(first: FigureN, second: FigureN): Boolean {
        return (first*second).exists()
    }


    private fun intoRectOval(rect: RectN, oval: OvalN): Boolean {
        return intoFigureOval(oval, FigureRectN(rect))
    }

    private fun intoRectFigure(rect: RectN, figure: FigureN): Boolean {
        return intoFigure(FigureRectN(rect), figure)
    }

    private fun intoFigureOval(oval: OvalN, figure: FigureN): Boolean {
        return figure.fields.all { f ->
            false
            //f.intoHalfS(oval.C, -oval.S[0]/2)
        }
        //todo
    }

    fun into(first: Shape, second: Shape): Boolean {
        if (!intoRect(rect(first), rect(second))) return false
        return when {
            first.code == Shape.Code.RECT && second.code == Shape.Code.RECT -> {
                true
            }

            first.code == Shape.Code.OVAL && second.code == Shape.Code.OVAL -> {
                intoOval(first as OvalN, second as OvalN)
            }

            first.code == Shape.Code.FIGURE && second.code == Shape.Code.FIGURE -> {
                intoFigure(first as FigureN, second as FigureN)
            }
            ////////////////////////////////////////////////////////////////////
            first.code == Shape.Code.OVAL && second.code == Shape.Code.RECT -> {
                intoRectOval(second as RectN, first as OvalN)
            }

            first.code == Shape.Code.RECT && second.code == Shape.Code.OVAL -> {
                intoRectOval(first as RectN, second as OvalN)
            }
            ////////////////////////////////////////////////////////////////////
            first.code == Shape.Code.OVAL && second.code == Shape.Code.FIGURE -> {
                intoFigureOval(first as OvalN, second as FigureN)
            }

            first.code == Shape.Code.FIGURE && second.code == Shape.Code.OVAL -> {
                intoFigureOval(second as OvalN, first as FigureN)
            }
            ////////////////////////////////////////////////////////////////////
            first.code == Shape.Code.FIGURE && second.code == Shape.Code.RECT -> {
                intoRectFigure(second as RectN, first as FigureN)
            }

            first.code == Shape.Code.RECT && second.code == Shape.Code.FIGURE -> {
                intoRectFigure(first as RectN, second as FigureN)
            }
            ////////////////////////////////////////////////////////////////////

            else -> throw DebugData.error("ERROR: illegal shape codes: ${first.code}, ${second.code}")
        }
    }

    fun interpolate(start: RectN, finish: RectN, k: Double): RectN {
        return RectN.LR(start.L.interpolate(finish.L, k), start.R.interpolate(finish.R, k))
    }

    fun rectX(first: RectN, second: RectN): RectN {
        fun minP(a: PointN, b: PointN) = PointN.transform(a, b) { x, y -> kotlin.math.min(x, y) }
        fun maxP(a: PointN, b: PointN) = PointN.transform(a, b) { x, y -> kotlin.math.max(x, y) }

        return RectN.LR(minP(first.L, second.L), maxP(first.R, second.R))
    }

    fun rectX(first: Shape, second: Shape): RectN {
        return rectX(rect(first), rect(second))
    }
}
