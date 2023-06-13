package com.uzery.fglib.core.obj.property

interface GameProperty<Type> {
    fun update()

    fun get(): Type
}