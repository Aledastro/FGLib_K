package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.math.num.IntI

class SpriteImage(origin: FGImage, err_name: String, val sprite_size: IntI, private vararg val effects: String):
    FGMultiImage(origin) {
    init {
        setData(origin.size/sprite_size)
        info = err_name
    }

    override fun from(pos: IntI) = ImageUtils.from(ImageUtils.split(origin, pos*sprite_size, sprite_size), *effects)
}
