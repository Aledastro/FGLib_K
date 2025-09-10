package com.uzery.fglib.core.obj

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.data.file.FGLibConst
import com.uzery.fglib.utils.data.file.TextData

/**
 * TODO("doc_un")
 **/
abstract class SmartGameObject<Type>: GameObject() {
    protected val objs = ArrayList<Type>()

    abstract val filename: String

    init {
        tag()
    }

    protected fun execute() {
        start()
        addComponent(*construct(objs).toTypedArray())
    }

    private fun start() {
        val data = if (TextData.existsFile(filename)) TextData[filename] else ArrayList()

        for (next in data) {
            if (FGUtils.isComment(next)) continue
            objs.add(from(next))
        }
    }

    fun onSave() {
        TextData.write(filename, data(), true)
    }

    protected abstract fun from(s: String): Type

    protected abstract fun construct(objs: ArrayList<Type>): List<ObjectComponent>

    fun data(): String {
        return buildString {
            append(FGLibConst.FILES_COMMENT)

            objs.forEach { append("$it\n") }
        }
    }
}
