package com.uzery.fglib.core.obj

import java.util.LinkedList

class SList<Type>: LinkedList<Type>() {
    fun add(vararg values: Type){
        addAll(values)
    }
}
