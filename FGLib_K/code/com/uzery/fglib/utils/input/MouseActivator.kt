package com.uzery.fglib.utils.input

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.MouseButton

abstract class MouseActivator(screen: RectN) {
    protected abstract fun pos0(): PointN
    val pos: PointN
        get() = pos0()

    abstract val keys: KeyActivator<MouseButton>
}
