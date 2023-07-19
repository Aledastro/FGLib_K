package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.data.file.ConstL.Companion.LITTLE
import com.uzery.fglib.utils.math.ShapeUtils.Companion.rect
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.geom.Shape
import kotlin.math.abs
import kotlin.math.min

interface CollisionUtils {
    companion object {
        fun maxMove(stay: Shape, start: Shape, finish: Shape): Double {
            if (!intoX(stay, start, finish)) return 1.0

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
            return ShapeUtils.into(stay, ShapeUtils.rectX(start, finish))
        }

        private fun maxMoveRect(stay: RectN, start: RectN, finish: RectN, level: Int): Double {
            val L1 = start.L[level]
            val R1 = start.R[level]
            val L2 = finish.L[level]
            val R2 = finish.R[level]
            val SL = stay.L[level]
            val SR = stay.R[level]
            val dim = stay.dimension()

            var path1 = if (abs(L1-L2) < LITTLE) 1.0 else (SR-L1)/(L2-L1)
            var path2 = if (abs(R1-R2) < LITTLE) 1.0 else (SL-R1)/(R2-R1)

            fun checkMoveOn(k: Double, lv: Int): Double {
                val shade = ShapeUtils.interpolate(start, finish, k)
                val blockedMove = stay.L[lv] < shade.R[lv] && shade.L[lv] < stay.R[lv] && k in (0.0..1.0)
                return if (blockedMove) k else 1.0
            }

            for (i in 1 until dim) {
                val lv = MathUtils.mod(level+i, dim)

                path1 = checkMoveOn(path1, lv)
                path2 = checkMoveOn(path2, lv)
            }

            return min(path1, path2).coerceIn(0.0, 1.0)
        }
    }
}