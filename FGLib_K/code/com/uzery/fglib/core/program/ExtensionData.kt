package com.uzery.fglib.core.program

import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.utils.graphics.AffineTransform
import com.uzery.fglib.utils.math.geom.PointN

class ExtensionData {
    var owner: Extension? = null
    val full_transform: AffineTransform
        get() {
            val transform = transform ?: AffineTransform.NEUTRAL
            val owner = owner ?: return transform
            return owner.data.full_transform*transform
        }

    internal var real_pos = PointN.ZERO
    internal val render_pos
        get() = pos + draw_pos

    //for updating
    var pos = PointN.ZERO
    var bounds: Bounds? = null
    var layout = FGLayout.BOTTOM_LEFT //todo

    var size = PointN.ZERO
    /*val size: PointN
        get() = bounds.size*/ //todo

    var transform: AffineTransform? = null


    //for drawing
    var draw_pos = PointN.ZERO
    var draw_size = PointN.ZERO
}
