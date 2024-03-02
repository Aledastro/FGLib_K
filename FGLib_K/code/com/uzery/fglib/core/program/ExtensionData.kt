package com.uzery.fglib.core.program

import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.utils.graphics.AffineTransform
import com.uzery.fglib.utils.math.geom.PointN

class ExtensionData {
    internal var render_pos = PointN.ZERO

    //for updating
    var pos = PointN.ZERO
    var bounds: Bounds? = null
    var layout = FGLayout.BOTTOM_LEFT //todo

    var size = PointN.ZERO
    /*val size: PointN
        get() = bounds.size*/ //todo

    var transform: AffineTransform? = null //todo
    var ch_transform: AffineTransform? = null //todo


    //for drawing
    var draw_pos = PointN.ZERO
    var draw_size = PointN.ZERO
}
