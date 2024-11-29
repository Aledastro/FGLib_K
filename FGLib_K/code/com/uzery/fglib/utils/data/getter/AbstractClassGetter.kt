package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.struct.num.StringN

abstract class AbstractClassGetter<Type> {
    operator fun get(entry: FGEntry): Type = getMark(entry)()
    operator fun get(input: String): Type = get(FGFormat[input])

    ///////////////////////////////////////////////////////////////////////////////////////////

    abstract fun getMark(entry: FGEntry): () -> Type

    abstract fun getEntryName(id: Int): StringN

    abstract fun getEntry(id: Int): () -> Type

    abstract fun entries_size(): Int
}
