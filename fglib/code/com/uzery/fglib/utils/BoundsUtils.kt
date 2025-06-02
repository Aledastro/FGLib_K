package com.uzery.fglib.utils

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.utils.CollisionUtils.MAX_MOVE_K
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

/**
 * TODO("doc")
 **/
object BoundsUtils {
    fun maxMoveOld(stay: Bounds, move: Bounds, stay_pos: PointN, start_pos: PointN, move_pos: PointN): Double {
        if (stay.elements.isEmpty() || move.elements.isEmpty()) return MAX_MOVE_K

        val r_stay = stay.cover_area ?: return MAX_MOVE_K
        val r_move = move.cover_area ?: return MAX_MOVE_K

        val end_pos = start_pos+move_pos

        if (!CollisionUtils.intoX(
                r_stay.copy(stay_pos),
                r_move.copy(start_pos),
                r_move.copy(end_pos)
            )
        ) return MAX_MOVE_K

        return stay.elements.minOf { stayE ->
            move.elements.minOf { moveE ->
                if (stayE.now == null || moveE.now == null) MAX_MOVE_K
                else {
                    val staySh = stayE.now!!
                    val moveSh = moveE.now!!
                    CollisionUtils.maxMove(staySh.copy(stay_pos), moveSh.copy(start_pos), moveSh.copy(end_pos))
                }
            }
        }
    }

    fun into(b1: Bounds, b2: Bounds, pos: PointN = PointN.ZERO): Boolean {
        return b1.elements.any { el1 ->
            if (el1.now == null) false
            else b2.elements.any { el2 ->
                if (el2.now == null) false
                else ShapeUtils.into(el1.now!!, el2.now!!.copy(pos))
            }
        }
    }

    fun intoShape(b1: Bounds, sh: Shape, pos: PointN = PointN.ZERO): Boolean {
        return b1.elements.any { el1 ->
            if (el1.now == null) false
            else ShapeUtils.into(el1.now!!, sh.copy(pos))
        }
    }
}
