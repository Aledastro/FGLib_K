package com.uzery.fglib.core.realisation.packager


/**
 * TODO("doc")
 **/
abstract class FGPackager {
    open val shader: FGShaderPackager? = null
    abstract val image: FGImagePackager
    abstract val audio: FGAudioPackager
}
