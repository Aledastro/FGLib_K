package com.uzery.fglib.utils.math

import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.utils.math.geom.PointN

interface BoundsUtils {
    companion object {
        fun maxMove(stay: Bounds, move: Bounds, stay_pos: PointN, start_pos: PointN, move_pos: PointN): Double {
            val r_stay = stay.main() ?: return 1.0
            val r_move = move.main() ?: return 1.0

            if(!CollisionUtils.intoX(r_stay, r_move, r_move.copy(move_pos))) return 1.0
            if(stay.elements.isEmpty() || move.elements.isEmpty()) return 1.0

            return stay.elements.minOf { stayE ->
                move.elements.minOf { moveE ->
                    if(stayE.shape() == null || moveE.shape() == null) 1.0
                    else {
                        val staySh = stayE.shape()!!
                        val moveSh = moveE.shape()!!
                        CollisionUtils.maxMove(
                            staySh.copy(stay_pos),
                            moveSh.copy(start_pos),
                            moveSh.copy(start_pos + move_pos))
                    }
                }
            }
        }
    }
}
