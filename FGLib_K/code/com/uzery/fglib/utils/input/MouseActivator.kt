package com.uzery.fglib.utils.input

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.MouseButton
import kotlin.math.sign

abstract class MouseActivator(screen: RectN) {
    protected abstract fun pos0(): PointN
    protected abstract fun scroll0(): PointN
    val pos: PointN
        get() = pos0()

    val scroll: Int
        get() = sign(scroll0().Y).toInt()


    abstract val keys: KeyActivator<MouseButton>
}
