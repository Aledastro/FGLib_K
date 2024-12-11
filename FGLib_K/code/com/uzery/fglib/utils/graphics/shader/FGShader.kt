package com.uzery.fglib.utils.graphics.shader

import com.uzery.fglib.core.program.Platform.packager
import com.uzery.fglib.utils.data.file.TextData

/**
 * TODO("doc")
 **/
class FGShader(shader_source: ArrayList<String>) {
    constructor(filename: String): this(TextData[filename])

    private val shp
        get() = packager.shader!!

    val source: Any = shp.createShader(shader_source)

    fun on() {
        shp.shaderOn(source)
    }

    fun off() {
        shp.shaderOff(source)
    }
}
