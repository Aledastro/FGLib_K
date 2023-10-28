package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
import com.uzery.fglib.utils.math.num.StringN
import java.util.ArrayList
import java.util.LinkedList

abstract class ParentClassGetter<Type>(private vararg val getters: ClassGetter<Type>): AbstractClassGetter<Type>() {

    private val sums = LinkedList<Int>()

    init {
        sums.add(0)
        for (get in getters){
            sums.addLast(get.entries_size()+sums.last)
        }
        sums.removeFirst()
    }

    final override fun getMark(name: String, args: ArrayList<ArrayList<String>>): () -> Type{
        return getters.firstOrNull { it.contains(StringN(name, args.size)) }?.getMark(name, args)
            ?: throw DebugData.error("ERROR parent getMark(): $name | $args")
    }

    final override fun getEntry(id: Int): () -> Type {
        val dest = getDest(id)
        return getters[dest.width].getEntry(dest.height)
    }

    final override fun entries_size(): Int {
        return sums.last
    }

    final override fun getEntryName(id: Int): StringN {
        val dest = getDest(id)
        return getters[dest.width].getEntryName(dest.height)
    }

    private fun getDest(id: Int): IntI{
        var getterID = 0
        for (i in getters.indices){
            getterID = i
            if(id<sums[i])break
        }
        return IntI(getterID, id - if(getterID>0) sums[getterID-1] else 0)
    }
}
