package com.uzery.fglib.utils.math

import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.utils.math.geom.PointN

interface BoundsUtils {
    companion object {
        fun maxMove(stay: Bounds, move: Bounds, stay_pos: PointN, start_pos: PointN, move_pos: PointN): Double {
            if(!CollisionUtils.intoX(stay.main(), move.main(), move.main().copy(move_pos))) return 1.0

            var min = 1.0
            for(stayE in stay.elements) {
                for(moveE in move.elements) {
                    min = min.coerceAtMost(
                        CollisionUtils.maxMove(
                            stayE.shape.copy(stay_pos),
                            moveE.shape.copy(start_pos),
                            moveE.shape.copy(start_pos + move_pos)))
                }
            }
            return min
        }
    }
}
