package com.uzery.fglib.core.program

import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.utils.graphics.AffineTransform
import com.uzery.fglib.utils.math.geom.PointN

class ExtensionData {
    var owner: Extension? = null
    internal val full_transform: AffineTransform
        get() {
            val transform = transform ?: AffineTransform.NEUTRAL
            val owner = owner ?: return transform
            return owner.data.full_transform*transform
        }

    internal val real_pos: PointN
        get() {
            val owner = owner ?: return transform?.pos(pos) ?: pos

            val transform = transform ?: return owner.data.real_pos+pos
            return owner.data.real_pos+transform.pos(pos)
        }

    internal val render_pos
        get() = pos+draw_pos

    //for updating
    var pos = PointN.ZERO
    var bounds: Bounds? = null
    var layout = FGLayout.BOTTOM_LEFT //todo

    var size = PointN.ZERO
    //todo: get() = if (bounds != null) bounds.size else field

    var transform: AffineTransform? = null


    //for drawing
    var draw_pos = PointN.ZERO
    var draw_size = PointN.ZERO
}
