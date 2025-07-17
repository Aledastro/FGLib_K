package com.uzery.fglib.utils.input

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.PlatformUpdatable
import com.uzery.fglib.utils.input.data.FGMouseKey
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.sign

/**
 * TODO("doc")
 **/
abstract class MouseActivator: PlatformUpdatable {
    protected abstract fun pos0(): PointN
    protected abstract fun scroll0(): PointN
    var pos: PointN = PointN.ZERO
        private set

    var scroll: Int = 0
        private set

    final override fun update() {
        scroll = sign(scroll0().Y).toInt()
        pos = Platform.options.resize_method.antiTransform(pos0())

        keys.update()
    }

    abstract val keys: KeyActivator<FGMouseKey>
}
