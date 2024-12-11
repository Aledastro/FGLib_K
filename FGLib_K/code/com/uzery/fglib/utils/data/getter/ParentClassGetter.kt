package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.struct.num.IntI
import com.uzery.fglib.utils.struct.num.StringN

/**
 * TODO("doc")
 **/
abstract class ParentClassGetter<Type>(private vararg val getters: ClassGetter<Type>): AbstractClassGetter<Type>() {
    private val sums = ArrayList<Int>()

    init {
        sums.add(0)
        for (get in getters) {
            sums.add(get.entries_size()+sums.last())
        }
        sums.removeFirst()
    }

    final override fun getMark(entry: FGEntry): () -> Type {
        return getters.firstOrNull { StringN(entry.name, entry.args.size) in it }?.getMark(entry)
            ?: throw DebugData.error("ERROR parent getMark(): $entry")
    }

    final override fun getEntry(id: Int): () -> Type {
        val dest = getDest(id)
        return getters[dest.x].getEntry(dest.y)
    }

    final override fun entries_size(): Int {
        return sums.last()
    }

    final override fun getEntryName(id: Int): StringN {
        val dest = getDest(id)
        return getters[dest.x].getEntryName(dest.y)
    }

    private fun getDest(id: Int): IntI {
        var getterID = 0
        for (pos in getters.indices) {
            getterID = pos
            if (id < sums[pos]) break
        }
        return IntI(getterID, id-if (getterID > 0) sums[getterID-1] else 0)
    }
}
