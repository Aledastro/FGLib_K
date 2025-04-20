package com.uzery.fglib.utils.struct

/**
 * TODO("doc")
 **/
class EnumVar<Type>(private vararg val var_sizes: Type) {
    fun fromValue(type: Type): Int {
        return var_sizes.indexOf(type)
    }

    fun fromID(id: Int): Type {
        return var_sizes[id]
    }

    fun toValue(type: Type) {
        id = fromValue(type)
    }

    fun toID(id: Int) {
        this.id = id
    }

    var id = -1
        set(value) {
            field = value.coerceIn(var_sizes.indices)
        }

    val value
        get() = var_sizes[id]
}
