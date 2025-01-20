package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.entry.FGFormat
import com.uzery.fglib.utils.struct.num.StringN

/**
 * TODO("doc")
 **/
abstract class AbstractClassGetter<Type> {
    operator fun get(entry: FGEntry): Type = getMark(entry)()
    operator fun get(input: String): Type = get(FGFormat.entryFrom(input))

    ///////////////////////////////////////////////////////////////////////////////////////////

    abstract fun getMark(entry: FGEntry): () -> Type

    abstract fun getEntryName(id: Int): StringN

    abstract fun getEntry(id: Int): () -> Type

    abstract fun entries_size(): Int
}
