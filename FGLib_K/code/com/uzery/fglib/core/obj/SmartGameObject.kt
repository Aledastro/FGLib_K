package com.uzery.fglib.core.obj

import com.uzery.fglib.utils.data.file.TextData

abstract class SmartGameObject<Type>(private val filename: String, name: String = "temp"): GameObject(name) {

    private val objs = ArrayList<Type>()

    init {
        start()

        addComponent(*this.construct(objs).toTypedArray())

        tag("#smart_object")
    }

    private fun start() {
        val data = TextData[filename]

        this.readInfo(data)

        var init = false
        for (next in data) {
            if (next == "objects:") init = true
            if (next == "" || next.startsWith("//")) continue
            if (init) objs.add(this.from(next))
        }
    }

    open fun readInfo(data: ArrayList<String>) {}
    abstract fun from(s: String): Type

    abstract fun construct(objs: ArrayList<Type>): List<ObjectComponent>

    override fun setValues() {
        values.add(filename)
    }
}
