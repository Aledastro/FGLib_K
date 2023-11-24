package com.uzery.fglib.utils.data.audio

import javafx.scene.media.MediaPlayer

object AudioBox {
    private val players = ArrayList<AudioClipControl>()
    fun addSFX(name: String, volume: Double = 1.0){

        AudioData.set(name)
        val player = MediaPlayer(AudioData[name])

        player.volume = volume
        val audio = AudioClipControl(player, volume)
        players.add(audio)
        player.onEndOfMedia = Runnable {
            audio.dead = true
        }
        player.play()
    }

    fun update(){
        players.removeIf { it.dead }
    }
}
