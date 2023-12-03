package com.uzery.fglib.utils.data.audio

import javafx.scene.media.Media

data class FGMedia(val name: String) {
    val source = Media(name)
}