package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

class SpriteImage(image: Image, name: String, val size: IntI): FGLImage() {
    constructor(filename: String, size: IntI): this(Image(filename), filename, size)

    private val origin: Image

    val origin_size: IntI

    init {
        origin_size = IntI(image.width.toInt(), image.height.toInt())
        origin = image

        setData(name, origin_size/size)
    }

    override fun from(pos: IntI) = ImageUtils.split(origin, pos*size, size)


}
