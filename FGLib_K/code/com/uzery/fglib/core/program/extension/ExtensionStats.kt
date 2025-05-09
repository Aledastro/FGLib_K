package com.uzery.fglib.core.program.extension

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.utils.graphics.AffineTransform
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
class ExtensionStats {
    fun next() {
        bounds?.next()
    }

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

    val real_pos: PointN
        get() {
            val owner = owner ?: return layout_pos
            val ownerP = owner.stats.real_pos
            return ownerP+layout_pos
        }

    internal val render_pos
        get() = layout_pos+draw_pos

    /////////////////////////////////////////////////////////////////////////////////////////

    //for updating
    var is_group = false
    private val owner_stats
        get() = owner!!.stats

    var pos = PointN.ZERO
        get() = if (is_group) owner_stats.pos else field
    var size = PointN.ZERO
        get() = if (is_group) owner_stats.size else field

    var bounds: Bounds? = null
        get() = if (is_group) null else field
    var layout = FGLayout.TOP_LEFT
        get() = if (is_group) owner_stats.layout else field

    /////////////////////////////////////////////////////////////////////////////////////////

    var transform: AffineTransform? = null

    var ui_level = UILevel.NEUTRAL

    //delta for drawing
    var draw_pos = PointN.ZERO
}
