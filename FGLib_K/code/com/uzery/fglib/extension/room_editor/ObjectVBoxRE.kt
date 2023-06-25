package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.scale
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.num.StringN
import javafx.scene.input.KeyCode
import java.util.*

class ObjectVBoxRE(private val data: DataGetterRE): VBox(0, 5) {
    val select_obj
        get() = data.get().getter.getEntry(chosen())

    //todo code is REALLY complicated
    private var t = 0
    override fun update() {
        if(t == 0) init0()
        t++

        if(keyboard.inPressed(KeyCode.DOWN)) {
            groups_select[groups[select].first] = groups_select[groups[select].first]!! + 1
        }
        if(keyboard.inPressed(KeyCode.UP)) {
            groups_select[groups[select].first] = groups_select[groups[select].first]!! - 1
        }
        coerceGroups()
    }

    override fun ifActiveUpdate() {

    }

    private fun coerceGroups() {
        for(pair in groups) {
            val size = groups_map[pair.first]!!.size
            groups_select[pair.first] = groups_select[pair.first]!!.coerceIn(0 until size)
        }
    }

    override val full
        get() = if(init0) groups.size else 0

    var init0 = false

    override val pos
        get() = PointN(data.get().OFFSET, 70.0)
    override val window: RectN
        get() = Platform.CANVAS_R
    override val sizeOne: PointN
        get() = PointN(50, 50)/scale

    override fun setNames(id: Int): String {
        return groups[id].first.s
    }

    override fun draw(pos: PointN, id: Int) {
        data.get().getter.getEntry(ids[groups[id].second[groups_select[groups[id].first]!!]]!!).draw(pos)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private fun init0() {
        entries = Array(data.get().getter.entry_size()) { data.get().getter.getEntryName(it) }

        for(id in entries.indices) ids[entries[id]] = id

        fun addNewEntry(name: StringN, entry: StringN) {
            val list = LinkedList<StringN>()
            list.add(entry)
            groups_map[name] = list
        }

        for(entry in entries) {
            if(!entry.s.contains("#")) {
                addNewEntry(entry, entry)
                continue
            }

            val name = StringN(FGUtils.subBefore(entry.s, "#"), entry.n)
            if(groups_map[name] == null) addNewEntry(name, entry)
            else groups_map[name]?.add(entry)
        }
        groups_map.keys.forEach { groups.add(Pair(it, groups_map[it]!!)) }

        for(pair in groups) {
            groups_select[pair.first] = 0
        }

        init0 = true
    }

    fun chosen(): Int {
        return ids[groups[select].second[groups_select[groups[select].first]!!]]!!
    }

    private lateinit var entries: Array<StringN>
    private val ids = TreeMap<StringN, Int>()
    private val groups_map = TreeMap<StringN, LinkedList<StringN>>()
    private val groups = LinkedList<Pair<StringN, LinkedList<StringN>>>()
    private val groups_select = TreeMap<StringN, Int>()
}