package com.uzery.fglib.core.room.mask

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.room.entry.FGRoomEntry
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.file.FGLibConst
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

/**
 * TODO("doc")
 **/
data class RoomMask(val name: String, private val actions: ArrayList<RoomChangeAction> = ArrayList()) {
    fun apply(
        room: Room,
        getter: AbstractClassGetter<GameObject>
    ) {
        for (action in actions) {
            when (action) {
                is RoomChangeAction.ADD -> {
                    room.objects.add(getter[action.obj])
                }

                is RoomChangeAction.REMOVE -> {
                    val obj_id = room.objects.indexOfFirst {
                        it.toEntry() == action.obj
                    }
                    room.objects.removeAt(obj_id)
                }
            }
        }
    }

    fun apply(room: FGRoomEntry) {
        for (action in actions) {
            when (action) {
                is RoomChangeAction.ADD -> room.objs.add(action.obj)
                is RoomChangeAction.REMOVE -> room.objs.remove(action.obj)
            }
        }
    }

    operator fun plus(other: RoomMask): RoomMask {
        val this_map = this.toMap()
        val other_map = other.toMap()
        val res = HashMap<FGEntry, Int>()

        res.putAll(this_map)
        other_map.forEach { (key, value) ->
            val current = res[key] ?: 0
            res[key] = current+value
        }
        return toMask(name, res)
    }

    operator fun minus(other: RoomMask): RoomMask {
        return this+(-other)
    }

    private operator fun unaryMinus(): RoomMask {
        val res = RoomMask(name)
        actions.forEach {
            res.actions.add(-it)
        }
        return res
    }

    private fun toMap(): HashMap<FGEntry, Int> {
        val map = HashMap<FGEntry, Int>()
        for (action in actions) {
            val current = map[action.obj] ?: 0

            when (action) {
                is RoomChangeAction.ADD -> {
                    map[action.obj] = current+1
                }

                is RoomChangeAction.REMOVE -> {
                    map[action.obj] = current-1
                }
            }
        }
        return map
    }

    private fun toMask(name: String, map: HashMap<FGEntry, Int>): RoomMask {
        val actions = ArrayList<RoomChangeAction>()
        map.forEach { (key, value) ->
            when {
                value < 0 -> {
                    for (i in 1..-value) {
                        actions.add(RoomChangeAction.REMOVE(key))
                    }
                }

                value > 0 -> {
                    for (i in 1..value) {
                        actions.add(RoomChangeAction.ADD(key))
                    }
                }
            }
        }
        return RoomMask(name, actions)
    }

    fun clear() {
        actions.clear()
    }

    fun toAdd(obj: FGEntry) {
        actions.add(RoomChangeAction.ADD(obj))
    }

    fun toRemove(obj: FGEntry) {
        actions.add(RoomChangeAction.REMOVE(obj))
    }

    fun isEmpty(): Boolean {
        return actions.isEmpty()
    }

    fun asMaskObject(): String {
        if (isEmpty()) return "mask[]"

        return buildString {
            append("mask[\n")
            actions.forEach { append("\t$it\n") }
            append("]")
        }
    }

    override fun toString(): String {
        return buildString {
            append(FGLibConst.FILES_COMMENT)

            actions.forEach { append("$it\n") }
        }
    }
}
