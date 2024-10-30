package com.uzery.fglib.core.realisation.packager

import com.uzery.fglib.utils.audio.FGAudio

abstract class FGAudioPackager {
    abstract fun createAudio(name: String): Any
    abstract fun playAudio(audio: FGAudio, volume: Double = 1.0)

}
