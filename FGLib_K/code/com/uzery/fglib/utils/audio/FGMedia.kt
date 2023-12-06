package com.uzery.fglib.utils.audio

import javafx.scene.media.Media

data class FGMedia(val name: String) {
    val source = Media(name)
}
