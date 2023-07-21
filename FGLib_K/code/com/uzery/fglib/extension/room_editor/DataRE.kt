package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.num.StringN
import java.util.*

class DataRE(val getter: ClassGetter<GameObject>, val filenames: Array<out String>) {
    var world_play = false
    var last_world_play = false
    var hide_ui = false
    var chosen_entry = 0
    lateinit var edit: Room
    var groupsValues: LinkedList<LinkedList<StringN>> = LinkedList<LinkedList<StringN>>()
    var groupsSelect: LinkedList<Int> = LinkedList<Int>()
    var draw_bounds = false
    var select_obj: GameObject? = null
    var chosen_obj: GameObject? = null
    var draw_pos = PointN.ZERO

    var edit_n = 0
    lateinit var last_edit_room: Room
    var last_edit_n: Int = 0

    var layers = LinkedList<DrawLayer>()
    var select_layer = 0
    var select_group = 0

    val OFFSET = 40.0
    val GRID
        get() = 32.0/Platform.scale
    val GRID_P
        get() = PointN(GRID, GRID)


    private val entries = LinkedList<StringN>()
    val names = LinkedList<StringN>()
    val ids = TreeMap<StringN, Int>()
    fun init() {
        entries.addAll(Array(getter.entry_size()) { getter.getEntryName(it) })

        for (id in entries.indices) ids[entries[id]] = id

        val groups_map = TreeMap<StringN, LinkedList<StringN>>()

        fun addNewEntry(name: StringN, entry: StringN) {
            val list = LinkedList<StringN>()
            list.add(entry)
            groups_map[name] = list
        }

        fun getName(entry: StringN): StringN {
            if (!entry.s.contains("#")) {
                return entry
            }
            return StringN(FGUtils.subBefore(entry.s, "#"), entry.n)
        }

        for (entry in entries) {
            val name = getName(entry)
            if (groups_map[name] == null) addNewEntry(name, entry)
            else groups_map[name]?.add(entry)
        }

        for (i in groups_map.keys) {
            groupsValues.add(LinkedList())
        }
        for ((id, key) in groups_map.keys.withIndex()) {
            val value = groups_map[key]!!
            groupsValues[id].addAll(value)
            names.add(getName(key))
        }

        for (i in groupsValues.indices) {
            groupsSelect.add(0)
        }
    }
}
