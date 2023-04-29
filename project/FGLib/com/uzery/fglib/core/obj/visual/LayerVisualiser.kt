package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer

abstract class LayerVisualiser(private val layer: DrawLayer): Visualiser {

    constructor(d: Double): this(DrawLayer(d))

    override fun drawLayer(): DrawLayer = layer
}
