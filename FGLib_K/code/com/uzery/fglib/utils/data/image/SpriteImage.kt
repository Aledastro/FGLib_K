package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

class SpriteImage(image: Image, name: String, val size: IntI, scale: Int = -1): FGLImage() {
    constructor(filename: String, size: IntI, scale: Int = -1): this(Image(filename), filename, size, scale)

    private val origin: Image

    private var SCALE = if (scale == -1) Platform.scale else scale

    val origin_size: IntI

    init {
        origin_size = IntI(image.width.toInt(), image.height.toInt())
        origin = image

        setData(name, origin_size/size)
    }

    override fun from(pos: IntI) = ImageUtils.split(origin, pos*size, size)


}
