package com.uzery.fglib.utils

import com.uzery.fglib.utils.ShapeUtils.rect
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.ConstL.LITTLE
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.abs
import kotlin.math.min

object CollisionUtils {
    const val MIN_MOVE_K = 0.0
    const val MAX_MOVE_K = 1.0
    const val SUPER_K = MAX_MOVE_K+LITTLE
    fun maxMove(stay: Shape, start: Shape, finish: Shape): Double {
        if (start.code != finish.code)
            throw DebugData.error("ERROR: illegal shape codes: ${stay.code}, ${start.code} -> ${finish.code}")

        if (!intoX(stay, start, finish)) return MAX_MOVE_K

        val max_move_rect = maxMoveRect(rect(stay), rect(start), rect(finish))

        if (max_move_rect == MAX_MOVE_K) return MAX_MOVE_K //todo important to keep this when will be more complex shape checks

        return max_move_rect

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
        return ShapeUtils.into(stay, ShapeUtils.mainOf(start, finish))
    }

    /*private fun maxMoveOval(stay: OvalN, start: OvalN, finish: OvalN): Double {
        return (0 until start.dim).minOf { level -> maxMoveOval(stay, start, finish, level) }
    }*/


    private fun maxMoveRect(stay: RectN, start: RectN, finish: RectN): Double {
        return (0..<start.dim).minOf { level -> maxMoveRect(stay, start, finish, level) }
    }

    private fun maxMoveRect(stay: RectN, start: RectN, finish: RectN, level: Int): Double {
        val L1 = start.L[level]
        val R1 = start.R[level]
        val L2 = finish.L[level]
        val R2 = finish.R[level]
        val SL = stay.L[level]
        val SR = stay.R[level]
        val dim = stay.dim

        var path1 = if (abs(L1-L2) < LITTLE) MAX_MOVE_K else (SR-L1)/(L2-L1)
        var path2 = if (abs(R1-R2) < LITTLE) MAX_MOVE_K else (SL-R1)/(R2-R1)

        fun checkMoveOn(k: Double, lv: Int): Double {
            val shade = ShapeUtils.interpolate(start, finish, k)
            val blockedMove = stay.L[lv] < shade.R[lv] && shade.L[lv] < stay.R[lv] && k in (MIN_MOVE_K..MAX_MOVE_K)
            return if (blockedMove) k else MAX_MOVE_K
        }

        for (i in 1..<dim) {
            val lv = MathUtils.mod(level+i, dim)

            path1 = checkMoveOn(path1, lv)
            path2 = checkMoveOn(path2, lv)
        }

        return min(path1, path2).coerceIn(MIN_MOVE_K, MAX_MOVE_K)
    }
}
