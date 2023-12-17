package com.uzery.fglib.core.obj

import com.uzery.fglib.utils.data.file.TextData

abstract class SmartGameObject<Type>(private val filename: String, name: String = "temp"): GameObject(name) {
    protected val objs = ArrayList<Type>()

    init {
        tag("#smart_object")
    }

    protected fun execute(){
        start()
        addComponent(*construct(objs).toTypedArray())
    }

    private fun start() {
        val data = TextData[filename]

        this.readInfo(data)

        var init = false
        for (next in data) {
            if (next == "objects:"){
                init = true
                continue
            }
            if (next == "" || next.startsWith("//")) continue
            if (init) objs.add(this.from(next))
        }
    }

    protected open fun readInfo(data: ArrayList<String>) {}
    protected abstract fun from(s: String): Type

    protected abstract fun construct(objs: ArrayList<Type>): List<ObjectComponent>

    override fun setValues() {
        values.add(filename)
    }
}
