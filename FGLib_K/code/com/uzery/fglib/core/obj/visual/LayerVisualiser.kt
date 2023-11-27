package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

abstract class LayerVisualiser(private val pos: PointN, private val size: PointN, private val layer: DrawLayer):
    Visualiser {
    override val main: RectN
        get() = RectN(pos, size)

    constructor(size: PointN, layer: DrawLayer): this(-size/2, size, layer)
    constructor(layer: DrawLayer): this(PointN.ZERO, PointN.ZERO, layer) //todo add main checker

    final override fun drawLayer(): DrawLayer = layer
}
