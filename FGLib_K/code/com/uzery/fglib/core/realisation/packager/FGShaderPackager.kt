package com.uzery.fglib.core.realisation.packager

abstract class FGShaderPackager {
    abstract fun createShader(shader_source: ArrayList<String>): Any
    abstract fun shaderOn(shader: Any)
    abstract fun shaderOff(shader: Any)
}
