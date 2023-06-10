package com.uzery.fglib.utils.data.image

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

class CombinationImage(name: String, val size: IntI, private val rule: ImageCombinationRule, scale: Int = -1):
    FGLImage() {

    private var SCALE = if(scale == -1) Platform.scale else scale
    private val origin: Image

    init {
        val image = Image(name)
        origin = ImageUtils.scale(image, IntI(SCALE, SCALE))

        setData(name, rule.count)
    }

    override fun from(pos: IntI): Image {
        val list = rule.from(pos)
        val array = Array(list.size) { ImageUtils.split(origin, list[it]*size*SCALE, size*SCALE) }

        val positions = rule.positions(pos) ?: return ImageUtils.combination(size*SCALE, array)
        return ImageUtils.combination(size*SCALE, array, positions)
    }
}
