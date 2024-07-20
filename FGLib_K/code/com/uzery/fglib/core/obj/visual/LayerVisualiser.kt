package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

abstract class LayerVisualiser(private val layer: DrawLayer): Visualiser() {
    constructor(pos: PointN, size: PointN, layer: DrawLayer): this(layer) {
        main = RectN(pos, size)
    }
    constructor(size: PointN, layer: DrawLayer): this(-size/2, size, layer)

    final override fun drawLayer(): DrawLayer = layer
}
