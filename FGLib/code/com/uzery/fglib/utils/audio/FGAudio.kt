package com.uzery.fglib.utils.audio

import com.uzery.fglib.core.program.Platform.packager

/**
 * TODO("doc")
 **/
data class FGAudio(val name: String) {
    val source = packager.audio.createAudio(name)
}
