package com.uzery.fglib.core.world.system.room

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.core.component.bounds.BoundsBox
import com.uzery.fglib.core.component.bounds.BoundsElement
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.UtilTags.util_inactive
import com.uzery.fglib.core.program.FGLibSettings.ROOM_ACTIVATE_GRID
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.system.WorldSystem
import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
class BasicRoomActivateSystem(
    private val has_overlap: Boolean,
    private val has_interrupt: Boolean,
    private val has_interact: Boolean,
    private val has_intersect: Boolean,
    private val has_impact: Boolean,
): WorldSystem() {
    private val RED = "RED"
    private val ORANGE = "ORANGE"
    private val BLUE = "BLUE"
    private val GREEN = "GREEN"
    private val GRAY = "GRAY"

    override fun updateRoom(room: Room) {
        val our = room.all_objs
            .filter { obj -> !obj.tagged(util_inactive) && obj.cover_area != null }

        if (has_overlap) {
            our
                .filter { !it.bounds.isEmpty(GRAY) }
                .forEach { obj ->
                    room.red_bounds.forEach { rbs ->
                        rbs.bounds.elements.forEach { el1 ->
                            obj.bounds[GRAY].elements.forEach { el2 ->
                                setActivate(obj, el2, rbs.o, el1, STD_OVERLAP)
                                setActivate(rbs.o, el1, obj, el2, STD_OVERLAP_I)
                            }
                        }
                    }
                }
        }

        activateOur(our)
    }

    private fun activateOur(our: List<GameObject>) {
        val cell_map = HashMap<IntI, ArrayList<GameObject>>()

        for (obj in our) {
            val area = obj.cover_area!!
            val L = (obj.stats.POS+area.L)/ROOM_ACTIVATE_GRID
            val R = (obj.stats.POS+area.R)/ROOM_ACTIVATE_GRID
            for (i in L.intX..R.intX) {
                for (j in L.intY..R.intY) {
                    val ii = IntI(i, j)
                    if (ii !in cell_map) cell_map[ii] = ArrayList()
                    cell_map[ii]!!.add(obj)
                }
            }
        }

        fun BoundsBox.collide(): Bounds {
            return if (isEmpty(RED)) get(ORANGE) else get(RED)
        }

        val checked = HashSet<Pair<GameObject, GameObject>>()
        fun checkFor(obj1: GameObject, obj2: GameObject) {
            if (obj1 == obj2) return

            val pc = Pair(obj1, obj2)

            if (pc in checked) return
            if (!ShapeUtils.into(obj1.cover_area!!.copy(obj1.stats.POS), obj2.cover_area!!.copy(obj2.stats.POS))) return

            if (has_interrupt) {
                obj1.bounds[BLUE].elements.forEach { el1 ->
                    obj2.bounds.collide().elements.forEach { el2 ->
                        setActivate(obj1, el1, obj2, el2, STD_INTERRUPT)
                        setActivate(obj2, el2, obj1, el1, STD_INTERRUPT_I)
                    }
                }
            }

            if (has_interact && obj1.interact()) {
                obj1.bounds.collide().elements.forEach { el1 ->
                    obj2.bounds[GREEN].elements.forEach { el2 ->
                        setActivate(obj2, el2, obj1, el1, STD_INTERACT)
                        setActivate(obj1, el1, obj2, el2, STD_INTERACT_I)
                    }
                }
            }
            if (has_intersect) {
                obj1.bounds[BLUE].elements.forEach { el1 ->
                    obj2.bounds[BLUE].elements.forEach { el2 ->
                        setActivate(obj1, el1, obj2, el2, STD_INTERSECT)
                        setActivate(obj2, el2, obj1, el1, STD_INTERSECT_I)
                    }
                }
            }

            if (has_impact) {
                obj1.bounds[ORANGE].elements.forEach { el1 ->
                    obj2.bounds[ORANGE].elements.forEach { el2 ->
                        setActivate(obj1, el1, obj2, el2, STD_IMPACT)
                        setActivate(obj2, el2, obj1, el1, STD_IMPACT_I)
                    }
                }
            }
        }

        cell_map.values.forEach { cell_objs ->
            for (i in cell_objs.indices) {
                for (j in cell_objs.indices) {
                    val pc = Pair(cell_objs[i], cell_objs[j])
                    checkFor(pc.first, pc.second)

                    checked.add(pc)
                }
            }
        }
    }

    private fun setActivate(o1: GameObject, sh1: BoundsElement, o2: GameObject, sh2: BoundsElement, code: String) {
        val shape1 = sh1.now ?: return
        val shape2 = sh2.now ?: return
        val copied1 = shape1.copy(o1.pos_with_owners)
        val copied2 = shape2.copy(o2.pos_with_owners)

        if (!ShapeUtils.into(copied1, copied2)) return
        if (sh1.group != sh2.group) return

        o1.activate(InputAction(code, o2, sh1.name, sh2.name))
    }

    companion object {
        const val STD_OVERLAP = "STD_OVERLAP"
        const val STD_OVERLAP_I = "STD_OVERLAP_I"

        const val STD_INTERRUPT = "STD_INTERRUPT"
        const val STD_INTERRUPT_I = "STD_INTERRUPT_I"

        const val STD_INTERACT = "STD_INTERACT"
        const val STD_INTERACT_I = "STD_INTERACT_I"

        const val STD_INTERSECT = "STD_INTERSECT"
        const val STD_INTERSECT_I = "STD_INTERSECT_I"

        const val STD_IMPACT = "STD_IMPACT"
        const val STD_IMPACT_I = "STD_IMPACT_I"
    }
}
