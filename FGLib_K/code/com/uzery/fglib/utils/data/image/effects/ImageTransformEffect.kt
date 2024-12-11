package com.uzery.fglib.utils.data.image.effects

import com.uzery.fglib.utils.data.image.WritableFGImage

/**
 * TODO("doc")
 **/
abstract class ImageTransformEffect(val name: String) {
    abstract operator fun get(origin: WritableFGImage, args: List<List<String>> = ArrayList()): WritableFGImage
}
