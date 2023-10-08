package com.uzery.fglib.utils.input

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.MouseButton

abstract class MouseActivator(screen: RectN) {
    protected abstract fun pos0(): PointN
    fun pos(): PointN {
        return pos0()
    }

    abstract val keys: KeyActivator<MouseButton>
}
