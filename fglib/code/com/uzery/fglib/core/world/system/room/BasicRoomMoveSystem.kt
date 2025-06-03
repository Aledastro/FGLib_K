package com.uzery.fglib.core.world.system.room

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.UtilTags.util_immovable
import com.uzery.fglib.core.room.PosBounds
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.system.WorldSystem
import com.uzery.fglib.utils.BoundsUtils
import com.uzery.fglib.utils.CollisionUtils.MAX_MOVE_K
import com.uzery.fglib.utils.CollisionUtils.SUPER_K
import com.uzery.fglib.utils.data.file.FGLibConst
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

/**
 * TODO("doc")
 **/
object BasicRoomMoveSystem: WorldSystem() {
    lateinit var red_bounds: ArrayList<PosBounds>

    fun getBS(objs: ArrayList<GameObject>): ArrayList<PosBounds> {
        val red_bounds = ArrayList<PosBounds>()

        objs.forEach { obj ->
            val bs = obj.bounds[UtilBounds.RED]
            if (!bs.empty) {
                red_bounds.add(PosBounds(obj.pos_with_owners, bs, obj))
            }
        }
        return red_bounds
    }

    fun allowPos(pos: PointN): Boolean {
        return !red_bounds.any { rbs ->
            rbs.bounds.into(pos-rbs.pos)
        }
    }

    fun allowShape(sh: Shape, pos: PointN = PointN.ZERO): Boolean {
        return !red_bounds.any { rbs ->
            BoundsUtils.intoShape(rbs.bounds, sh, pos-rbs.pos)
        }
    }

    fun allowBounds(bs: Bounds, pos: PointN = PointN.ZERO): Boolean {
        return !red_bounds.any { rbs ->
            BoundsUtils.into(rbs.bounds, bs, pos-rbs.pos)
        }
    }

    override fun updateRoom(room: Room) {
        red_bounds = getBS(room.all_objs)

        for (obj in room.all_objs) {
            if (obj.tagged(util_immovable)) continue

            obj.stats.fly = true
            obj.stats.fly_by.clear()

            obj.stats.lastPOS = obj.stats.POS
            val move_bs = obj.bounds[UtilBounds.ORANGE]
            if (obj.stats.nPOS.length() < FGLibConst.LITTLE) continue

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

            fun move(move_p: PointN) = maxMove(move_p*SUPER_K).also { k ->
                obj.stats.POS += move_p*k
            }

            val min_d = move(obj.stats.nPOS)
            obj.stats.fly = min_d == MAX_MOVE_K

            var np = obj.stats.nPOS*(1-min_d)

            if (obj.stats.sticky) {
                val sticky_by_default = false
                val sticky = PointN(Array(np.dim) { i ->
                    if (obj.stats.sticky_by[i] ?: sticky_by_default) 1.0 else 0.0
                })
                np *= sticky
            }

            for (level in 0..<np.dim) {
                val nnp = np.separate(level)

                if (nnp.length() < FGLibConst.LITTLE) {
                    obj.stats.fly_by[level] = true
                    continue
                }

                val m = move(nnp)
                obj.stats.fly_by[level] = m == MAX_MOVE_K
            }
        }
        room.all_objs.forEach { it.stats.nPOS = PointN.ZERO }
    }
}
