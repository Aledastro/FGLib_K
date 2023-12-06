package com.uzery.fglib.utils.audio

import com.uzery.fglib.core.program.Platform.packager

data class FGAudio(val name: String) {
    val source = packager.audio.createAudio(name)
}
