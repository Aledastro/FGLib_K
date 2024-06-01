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
            return owner.stats.full_transform*transform
        }

    private val layout_pos: PointN
        get() {
            val s_pos = transform?.pos(pos) ?: pos

            val owner = owner ?: return s_pos
            val layoutP = (owner.stats.size-size)*layout.value

            return layoutP+s_pos
        }

    internal val real_pos: PointN
        get() {
            val owner = owner ?: return layout_pos
            val ownerP = owner.stats.real_pos
            return ownerP+layout_pos
        }

    internal val render_pos
        get() = layout_pos+draw_pos

    //for updating
    var pos = PointN.ZERO
    var bounds: Bounds? = null
    var layout = FGLayout.TOP_LEFT

    var size = PointN.ZERO
    //todo: get() = if (bounds != null) bounds.size else field

    var transform: AffineTransform? = null


    //delta for drawing
    var draw_pos = PointN.ZERO
}
