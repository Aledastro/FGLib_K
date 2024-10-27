package com.uzery.fglib.core.room.mask

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.ConstL
import com.uzery.fglib.utils.data.getter.AbstractClassGetter

data class RoomMask(private val actions: ArrayList<RoomChangeAction> = ArrayList()) {
    fun apply(room: Room, getter: AbstractClassGetter<GameObject>) {
        for (action in actions) {
            when (action.sign) {
                "+" -> {
                    room.objects.add(getter[action.obj])
                }

                "-" -> {
                    val obj_id = room.objects.indexOfLast {
                        it.toString() == action.obj
                    }
                    room.objects.removeAt(obj_id)
                }

                else -> throw DebugData.error("unsupported sign: ${action.sign}")
            }
        }
    }

    operator fun plus(other: RoomMask): RoomMask {
        val this_map = this.toMap()
        val other_map = other.toMap()
        val res = HashMap<String, Int>()

        res.putAll(this_map)
        other_map.forEach { (key, value) ->
            val current = res[key] ?: 0
            res[key] = current+value
        }
        return toMask(res)
    }

    operator fun minus(other: RoomMask): RoomMask {
        return this+(-other)
    }

    private operator fun unaryMinus(): RoomMask {
        val res = RoomMask()
        actions.forEach {
            res.actions.add(-it)
        }
        return res
    }

    private fun toMap(): HashMap<String, Int> {
        val map = HashMap<String, Int>()
        for (action in actions) {
            val current = map[action.obj] ?: 0

            when (action.sign) {
                "+" -> map[action.obj] = current+1
                "-" -> map[action.obj] = current-1
            }
        }
        return map
    }

    private fun toMask(map: HashMap<String, Int>): RoomMask {
        val actions = ArrayList<RoomChangeAction>()
        map.forEach { (key, value) ->
            when {
                value < 0 -> {
                    for (i in 1..-value) {
                        actions.add(RoomChangeAction("- $key"))
                    }
                }

                value > 0 -> {
                    for (i in 1..value) {
                        actions.add(RoomChangeAction("+ $key"))
                    }
                }
            }
        }
        return RoomMask(actions)
    }

    fun clear() {
        actions.clear()
    }

    fun toAdd(obj: String) {
        actions.add(RoomChangeAction("+ $obj"))
    }

    fun toRemove(obj: String) {
        actions.add(RoomChangeAction("- $obj"))
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
            append(ConstL.FILES_COMMENT)

            actions.forEach { append("$it\n") }
        }
    }
}
