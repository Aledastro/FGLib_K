package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.ConstL.LITTLE
import com.uzery.fglib.utils.math.ShapeUtils.rect
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.abs
import kotlin.math.min

object CollisionUtils {
    fun maxMove(stay: Shape, start: Shape, finish: Shape): Double {
        if (start.code != finish.code)
            throw DebugData.error("ERROR: illegal shape codes: ${stay.code}, ${start.code} -> ${finish.code}")

        if (!intoX(stay, start, finish)) return 1.0

        val maxMoveRect = maxMoveRect(rect(stay), rect(start), rect(finish))

        if (maxMoveRect == 1.0) return maxMoveRect

        return maxMoveRect

        /*return when{
            stay.code == Shape.Code.RECT && start.code == Shape.Code.RECT -> {
                maxMoveRect
            }

            stay.code == Shape.Code.OVAL && start.code == Shape.Code.OVAL -> {
                //maxMoveOval(stay as OvalN, start as OvalN, finish as OvalN)
                maxMoveRect
            }

            stay.code == Shape.Code.OVAL && start.code == Shape.Code.RECT -> {
                //maxMoveOvalRect(stay as OvalN, start as RectN, finish as RectN)
                maxMoveRect
            }

            stay.code == Shape.Code.RECT && start.code == Shape.Code.OVAL -> {
                //maxMoveRectOval(stay as RectN, start as OvalN, finish as OvalN)
                maxMoveRect
            }

            else -> throw DebugData.error("ERROR: illegal shape codes: ${stay.code}, ${start.code} -> ${finish.code}")
        }*/
    }

    fun intoX(stay: Shape, start: Shape, finish: Shape): Boolean {
        return ShapeUtils.into(stay, ShapeUtils.rectX(start, finish))
    }

    /*private fun maxMoveOval(stay: OvalN, start: OvalN, finish: OvalN): Double {
        return (0 until start.dim).minOf { level -> maxMoveOval(stay, start, finish, level) }
    }*/


    private fun maxMoveRect(stay: RectN, start: RectN, finish: RectN): Double {
        return (0 until start.dim).minOf { level -> maxMoveRect(stay, start, finish, level) }
    }

    private fun maxMoveRect(stay: RectN, start: RectN, finish: RectN, level: Int): Double {
        val L1 = start.L[level]
        val R1 = start.R[level]
        val L2 = finish.L[level]
        val R2 = finish.R[level]
        val SL = stay.L[level]
        val SR = stay.R[level]
        val dim = stay.dim

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
