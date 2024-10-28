package com.uzery.fglib.utils.graphics.shader

import com.uzery.fglib.core.program.Platform.packager
import com.uzery.fglib.utils.data.file.TextData

abstract class FGShader(shader_source: ArrayList<String>) {
    constructor(filename: String): this(TextData[filename])

    val sh
        get() = packager.shader!!

    val source: Any = sh.createShader(shader_source)

    fun on() {
        sh.shaderOn(source)
    }

    fun off() {
        sh.shaderOff(source)
    }
}
