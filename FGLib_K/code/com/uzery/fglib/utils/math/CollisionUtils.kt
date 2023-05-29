package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.math.ShapeUtils.Companion.rect
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.geom.Shape
import kotlin.math.abs

interface CollisionUtils {
    companion object {
        private const val LITTLE = 0.00001

        fun maxMove(stay: Shape, start: Shape, finish: Shape): Double {
            if(!intoX(stay, start, finish)) return 1.0

            //todo when(shape)
            return maxMoveRect(rect(stay), rect(start), rect(finish))
        }

        private fun maxMoveRect(stay: RectN, start: RectN, finish: RectN): Double {
            return (0 until start.dimension()).minOf { level -> maxMoveRect(stay, start, finish, level) }
        }

        fun intoX(stay: Shape, start: Shape, finish: Shape): Boolean {
            //todo when(shape)
            return intoX(rect(stay), rect(start), rect(finish))
        }

        private fun intoX(stay: RectN, start: RectN, finish: RectN): Boolean {
            return ShapeUtils.into(stay, RectN.rectLR(min(start.L, finish.L), max(start.R, finish.R)))
        }

        private fun min(a: PointN, b: PointN) = PointN.transform(a, b) { x, y -> MathUtils.min(x, y) }
        private fun max(a: PointN, b: PointN) = PointN.transform(a, b) { x, y -> MathUtils.max(x, y) }

        private fun maxMoveRect(stay: RectN, start: RectN, finish: RectN, level: Int): Double {
            var path1 = 1.0
            var path2 = 1.0

            val fl = start.L[level]
            val fr = start.R[level]
            val sl = finish.L[level]
            val sr = finish.R[level]

            val l = stay.L[level]
            val r = stay.R[level]

            if(abs(fl - sl)>LITTLE) path1 = (r - fl)/(sl - fl)
            if(abs(fr - sr)>LITTLE) path2 = (l - fr)/(sr - fr)

            val n = stay.dimension()
            for(i in 1 until n) {
                val lv = MathUtils.mod(level + i, n)
                val rect1 = ShapeUtils.interpolate(start, finish, path1)
                if(!(stay.L[lv]<=rect1.R[lv] && stay.R[lv]>=rect1.L[lv] && path1 in (0.0..1.0))) path1 = 1.0

                val rect2 = ShapeUtils.interpolate(start, finish, path2)
                if(!(stay.L[lv]<=rect2.R[lv] && stay.R[lv]>=rect2.L[lv] && path2 in (0.0..1.0))) path2 = 1.0
            }

            val path = kotlin.math.min(path1, path2)

            if(path<0) return 0.0
            if(path>1) return 1.0 //throw DebugData.error("err: K=$path")

            return path.coerceIn(0.0, 1.0)
        }
    }
}