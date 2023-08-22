package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.*
import kotlin.math.pow

object ShapeUtils {
    fun rect(shape: Shape) = RectN(shape.L, shape.S)
    fun oval(shape: Shape) = OvalN(shape.C, shape.S)

    fun intoRect(first: RectN, second: RectN): Boolean {
        return (0 until first.dimension()).all { i -> first.L[i] < second.R[i] && second.L[i] < first.R[i] }
    }

    private fun intoOval(first: OvalN, second: OvalN): Boolean {
        //todo circleN
        return (first.C-second.C).length() < (first.S[0]+second.S[0])/2
    }

    private fun intoRectOval(first: RectN, second: OvalN): Boolean {
        if(intoOval(oval(first), second)) return true

        val dim = first.dimension()

        for (id in 0 until 2.0.pow(dim).toInt()){
            var id_n=id
            val xs = Array(dim){ 0.0 }
            for (i in 0 until dim){
                xs[i]=if(id_n%2==0) first.L[i] else first.R[i]
                id_n/=2
            }
            val p = PointN(xs)
            if(p.lengthTo(second.C)<second.S[0]) return true
        }

        return false
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

            first.code == Shape.Code.LINE && second.code == Shape.Code.LINE -> {
                PointN.isSameDirection(first.L-second.L,first.S-second.S)
                //L1+S1*k1 = L2+S2*k2
                //L1-L2 = (S2-S1)*k
            }
            ////////////////////////////////////////////////////////////////////
            first.code == Shape.Code.OVAL && second.code == Shape.Code.RECT -> {
                intoRectOval(second as RectN, first as OvalN)
            }

            first.code == Shape.Code.RECT && second.code == Shape.Code.OVAL -> {
                intoRectOval(first as RectN, second as OvalN)
            }
            ////////////////////////////////////////////////////////////////////
            first.code == Shape.Code.LINE && second.code == Shape.Code.RECT -> {
                PointN.isSameDirection(first.L-second.L,first.S-second.S)
            }
            first.code == Shape.Code.RECT && second.code == Shape.Code.LINE -> {
                PointN.isSameDirection(first.L-second.L,first.S-second.S)
            }
            ////////////////////////////////////////////////////////////////////
            first.code == Shape.Code.LINE && second.code == Shape.Code.OVAL -> {
                PointN.isSameDirection(first.L-second.L,first.S-second.S)
            }
            first.code == Shape.Code.OVAL && second.code == Shape.Code.LINE -> {
                PointN.isSameDirection(first.L-second.L,first.S-second.S)
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
