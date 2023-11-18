package com.uzery.fglib.utils.data.image.effects

import javafx.scene.image.WritableImage
import java.util.*

abstract class ImageTransformEffect(val name: String) {
    abstract operator fun get(origin: WritableImage, args: List<List<String>> = ArrayList()): WritableImage
}
