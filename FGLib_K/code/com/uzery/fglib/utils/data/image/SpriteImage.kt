package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

class SpriteImage(image: Image, name: String, val size: IntI, private vararg val effects: String): FGLImage() {
    constructor(filename: String, size: IntI, vararg effects: String): this(ImageUtils.from(filename), filename, size, *effects)

    private val origin: Image

    val origin_size: IntI

    init {
        origin_size = IntI(image.width.toInt(), image.height.toInt())
        origin = image

        setData(name, origin_size/size)
    }

    override fun from(pos: IntI) = ImageUtils.from(ImageUtils.split(origin, pos*size, size), *effects)
}
