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

    fun toGroup() {
        val st = owner!!.stats

        pos = st.pos
        size = st.size
        layout = st.layout

        bounds = null
    }

    //for updating
    var pos = PointN.ZERO
    var size = PointN.ZERO
    var layout = FGLayout.TOP_LEFT

    var bounds: Bounds? = null

    /////////////////////////////////////////////////////////////////////////////////////////

    var transform: AffineTransform? = null

    var ui_level = UILevel.NEUTRAL

    //delta for drawing
    var draw_pos = PointN.ZERO
}
