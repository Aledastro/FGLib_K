package com.uzery.fglib.utils.data.audio

import com.uzery.fglib.core.program.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

//todo into JavaFX Realisation
class FGMediaPlayer(val media: FGMedia, val volume: Double) {
    val player = MediaPlayer(media.source)

    init {
        player.volume = volume
    }

    var onEndOfMedia = Runnable {}
        set(value) {
            player.onEndOfMedia = value
            field = value
        }

    var dead = false

    fun play() {
        player.play()
    }
}
