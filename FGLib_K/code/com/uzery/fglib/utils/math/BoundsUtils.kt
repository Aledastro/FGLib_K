package com.uzery.fglib.utils.math

import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.utils.math.CollisionUtils.MAX_MOVE_K
import com.uzery.fglib.utils.math.geom.PointN

object BoundsUtils {
    fun maxMoveOld(stay: Bounds, move: Bounds, stay_pos: PointN, start_pos: PointN, move_pos: PointN): Double {
        if (stay.elements.isEmpty() || move.elements.isEmpty()) return MAX_MOVE_K

        val r_stay = stay.main() ?: return MAX_MOVE_K
        val r_move = move.main() ?: return MAX_MOVE_K

        val end_pos = start_pos+move_pos

        if (!CollisionUtils.intoX(
                r_stay.copy(stay_pos),
                r_move.copy(start_pos),
                r_move.copy(end_pos)
            )
        ) return MAX_MOVE_K

        return stay.elements.minOf { stayE ->
            move.elements.minOf { moveE ->
                if (stayE.shape() == null || moveE.shape() == null) MAX_MOVE_K
                else {
                    val staySh = stayE.shape()!!
                    val moveSh = moveE.shape()!!
                    CollisionUtils.maxMove(staySh.copy(stay_pos), moveSh.copy(start_pos), moveSh.copy(end_pos))
                }
            }
        }
    }
}
