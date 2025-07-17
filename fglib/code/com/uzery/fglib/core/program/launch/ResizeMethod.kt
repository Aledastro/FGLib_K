package com.uzery.fglib.core.program.launch

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.min

sealed class ResizeMethod {
    abstract fun transform(p: PointN): PointN
    abstract fun antiTransform(p: PointN): PointN
    abstract fun transformSize(p: PointN): PointN
    abstract fun start_size(): PointN

    class PIXEL_PERFECT(scale: Int): ResizeMethod() {
        val real_scale = if (scale == -1) {
            val size = Platform.SCREEN/Platform.CANVAS
            min(size.intX, size.intY)
        } else scale

        override fun transform(p: PointN): PointN {
            return p*real_scale
        }

        override fun transformSize(p: PointN): PointN {
            return p*real_scale
        }

        override fun antiTransform(p: PointN): PointN {
            return p/real_scale
        }

        override fun start_size(): PointN {
            return Platform.options.canvas_size*real_scale
        }
    }

    class STRETCH(val view_size: PointN): ResizeMethod() {
        val k
            get() = Platform.WINDOW/Platform.CANVAS
        override fun transform(p: PointN): PointN {
            return p*k
        }

        override fun transformSize(p: PointN): PointN {
            return p*min(k.X, k.Y)
        }

        override fun antiTransform(p: PointN): PointN {
            return p/k
        }

        override fun start_size(): PointN {
            return view_size
        }
    }

    class STRETCH_FIXED(val view_size: PointN): ResizeMethod() {
        override fun transform(p: PointN): PointN {
            return p //todo
        }

        override fun transformSize(p: PointN): PointN {
            return p
        }

        override fun antiTransform(p: PointN): PointN {
            return p //todo
        }

        override fun start_size(): PointN {
            return view_size
        }
    }
}
