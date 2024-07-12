package com.uzery.fglib.utils.input

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.input.data.FGMouseKey
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.sign

abstract class MouseActivator {
    protected abstract fun pos0(): PointN
    protected abstract fun scroll0(): PointN
    val pos: PointN
        get() = pos0()/graphics.scale

    val scroll: Int
        get() = sign(scroll0().Y).toInt()


    abstract val keys: KeyActivator<FGMouseKey>
}
