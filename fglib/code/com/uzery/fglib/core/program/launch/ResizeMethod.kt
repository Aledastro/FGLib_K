package com.uzery.fglib.core.program.launch

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.min

sealed class ResizeMethod {
    abstract fun transform(p: PointN): PointN
    abstract fun antiTransform(p: PointN): PointN
    abstract fun transformSize(p: PointN): PointN
    abstract fun start_size(): PointN
    open fun adapt(size: PointN): PointN = size

    val CANVAS_SIZE
        get() = Platform.options.canvas_size

    open class PIXEL_PERFECT(scale: Int): ResizeMethod() {
        val real_scale = if (scale == -1) {
            val size = Platform.SCREEN/CANVAS_SIZE
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
            return CANVAS_SIZE*real_scale
        }

        class AUTO: PIXEL_PERFECT(scale = -1)
    }

    class STRETCH(val view_size: PointN): ResizeMethod() {
        val k_pos
            get() = Platform.WINDOW/CANVAS_SIZE

        override fun transform(p: PointN): PointN {
            return p*k_pos
        }

        override fun transformSize(p: PointN): PointN {
            return p*min(k_pos.X, k_pos.Y)
        }

        override fun antiTransform(p: PointN): PointN {
            return p/k_pos
        }

        override fun start_size(): PointN {
            return view_size
        }
    }

    class STRETCH_FIXED(val view_size: PointN): ResizeMethod() {
        val k
            get() = min(Platform.WINDOW.X/CANVAS_SIZE.X, Platform.WINDOW.Y/CANVAS_SIZE.Y)

        override fun transform(p: PointN): PointN {
            return p*k
        }

        override fun transformSize(p: PointN): PointN {
            return p*k
        }

        override fun antiTransform(p: PointN): PointN {
            return p/k
        }

        override fun start_size(): PointN {
            return view_size
        }

        override fun adapt(size: PointN): PointN {
            val adapt_k = (Platform.WINDOW/CANVAS_SIZE)/k
            return size*adapt_k
        }
    }
}
