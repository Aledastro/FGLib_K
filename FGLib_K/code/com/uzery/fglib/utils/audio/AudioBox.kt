package com.uzery.fglib.utils.audio

import com.uzery.fglib.core.program.Platform.packager

object AudioBox {
    fun addSFX(name: String, volume: Double = 1.0) {

        AudioData.set(name)
        packager.audio.playAudio(AudioData[name], volume)
    }
}
