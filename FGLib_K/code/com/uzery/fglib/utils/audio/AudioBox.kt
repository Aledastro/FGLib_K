package com.uzery.fglib.utils.audio

object AudioBox {
    private val players = ArrayList<FGMediaPlayer>()
    fun addSFX(name: String, volume: Double = 1.0) {

        AudioData.set(name)
        val player = FGMediaPlayer(AudioData[name], volume)

        players.add(player)
        player.onEndOfMedia = Runnable {
            player.dead = true
        }
        player.play()
    }

    fun update() {
        players.removeIf { it.dead }
    }
}
