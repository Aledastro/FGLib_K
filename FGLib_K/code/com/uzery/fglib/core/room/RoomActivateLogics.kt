package com.uzery.fglib.core.room

import com.uzery.fglib.core.component.bounds.BoundsElement
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.FGLibSettings.ROOM_ACTIVATE_GRID
import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
internal object RoomActivateLogics {
    fun nextActivate(objs: ArrayList<GameObject>) {
        val cell_map = HashMap<IntI, ArrayList<GameObject>>()

        val our = objs.filter { obj -> !obj.tagged("#inactive") && obj.main != null }

        for (obj in our) {
            val main = obj.main!!
            val L = (obj.stats.POS+main.L)/ROOM_ACTIVATE_GRID
            val R = (obj.stats.POS+main.R)/ROOM_ACTIVATE_GRID
            for (i in L.intX..R.intX) {
                for (j in L.intY..R.intY) {
                    val ii = IntI(i, j)
                    if (ii !in cell_map) cell_map[ii] = ArrayList()
                    cell_map[ii]!!.add(obj)
                }
            }
        }

        val checked = HashSet<Pair<GameObject, GameObject>>()
        fun checkFor(obj1: GameObject, obj2: GameObject) {
            if (obj1 == obj2) return

            val pc = Pair(obj1, obj2)

            if (pc in checked) return
            if (!ShapeUtils.into(obj1.main!!.copy(obj1.stats.POS), obj2.main!!.copy(obj2.stats.POS))) return

            obj1.bounds.blue.elements.forEach { el1 ->
                obj2.bounds.main.elements.forEach { el2 ->
                    setActivate(obj1, el1, obj2, el2, "#INTERRUPT")
                    setActivate(obj2, el2, obj1, el1, "#INTERRUPT_I")
                }
            }

            if (obj1.interact()) {
                obj1.bounds.main.elements.forEach { el1 ->
                    obj2.bounds.green.elements.forEach { el2 ->
                        setActivate(obj2, el2, obj1, el1, "#INTERACT")
                        setActivate(obj1, el1, obj2, el2, "#INTERACT_I")
                    }
                }
            }

            obj1.bounds.blue.elements.forEach { el1 ->
                obj2.bounds.blue.elements.forEach { el2 ->
                    setActivate(obj1, el1, obj2, el2, "#INTERSECT")
                    setActivate(obj2, el2, obj1, el1, "#INTERSECT_I")
                }
            }

            obj1.bounds.orange.elements.forEach { el1 ->
                obj2.bounds.orange.elements.forEach { el2 ->
                    setActivate(obj1, el1, obj2, el2, "#IMPACT")
                    setActivate(obj2, el2, obj1, el1, "#IMPACT_I")
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
}
