package com.uzery.fglib.utils.input

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.input.data.FGMouseKey
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.sign

abstract class MouseActivator {
    protected abstract fun pos0(): PointN
    protected abstract fun scroll0(): PointN
    val pos: PointN
        get() = pos0()/Platform.scale

    val scroll: Int
        get() = sign(scroll0().Y).toInt()


    abstract val keys: KeyActivator<FGMouseKey>
}
