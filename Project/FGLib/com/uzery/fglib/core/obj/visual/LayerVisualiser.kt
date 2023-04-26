package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer

abstract class LayerVisualiser(private val layer: DrawLayer): Visualiser {
    override fun drawLayer(): DrawLayer = layer
}
