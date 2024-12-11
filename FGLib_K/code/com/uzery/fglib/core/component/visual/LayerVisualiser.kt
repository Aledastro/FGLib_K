package com.uzery.fglib.core.component.visual

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * TODO("doc")
 **/
abstract class LayerVisualiser(private val layer: DrawLayer): Visualiser() {
    constructor(pos: PointN, size: PointN, layer: DrawLayer): this(layer) {
        main = RectN(pos, size)
    }

    constructor(size: PointN, layer: DrawLayer): this(PointN.ZERO, size, layer)

    final override fun drawLayer(): DrawLayer = layer
}
