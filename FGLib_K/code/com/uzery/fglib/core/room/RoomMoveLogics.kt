package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.UtilTags.util_immovable
import com.uzery.fglib.utils.BoundsUtils
import com.uzery.fglib.utils.CollisionUtils.MAX_MOVE_K
import com.uzery.fglib.utils.CollisionUtils.SUPER_K
import com.uzery.fglib.utils.data.file.FGLibConst
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
internal object RoomMoveLogics {
    fun getBS(objs: ArrayList<GameObject>): ArrayList<PosBounds> {
        val red_bounds = ArrayList<PosBounds>()

        objs.forEach { obj ->
            val bs = obj.bounds.red
            if (!bs.empty) {
                red_bounds.add(PosBounds(obj.pos_with_owners, bs, obj))
            }
        }
        return red_bounds
    }

    fun nextMoveOld(red_bounds: ArrayList<PosBounds>, objs: ArrayList<GameObject>) {
        for (obj in objs) {
            if (obj.tagged(util_immovable)) continue
            obj.stats.lPOS = obj.stats.POS
            val move_bs = obj.bounds.orange

            if (move_bs.empty) {
                obj.stats.POS += obj.stats.nPOS
                continue
            }

            fun maxMove(move_p: PointN): Double {
                if (red_bounds.isEmpty()) return MAX_MOVE_K

                return red_bounds.indices.minOf { i ->
                    BoundsUtils.maxMoveOld(
                        red_bounds[i].bounds,
                        move_bs,
                        red_bounds[i].pos,
                        obj.pos_with_owners,
                        move_p
                    )
                }
            }

            fun move(move_p: PointN): Double {
                if (move_p.length() < FGLibConst.LITTLE) return MAX_MOVE_K

                val mm = maxMove(move_p*SUPER_K)
                obj.stats.POS += move_p*mm
                return mm
            }

            val min_d = move(obj.stats.nPOS)

            val is_move_complete = min_d == MAX_MOVE_K

            obj.stats.fly = is_move_complete

            var np = obj.stats.nPOS*(1-min_d)

            if (obj.stats.sticky) {
                val sticky_by_default = false
                val sticky = PointN(Array(np.dim) { i ->
                    if (obj.stats.sticky_by[i] ?: sticky_by_default) 1.0 else 0.0
                })
                np *= sticky
            }

            obj.stats.fly_by.clear()

            for (level in 0..<np.dim) {
                val m = move(np.separate(level))

                obj.stats.fly_by[level] = m == MAX_MOVE_K
            }
        }
        objs.forEach { it.stats.nPOS = PointN.ZERO }
    }
}
