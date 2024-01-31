package com.uzery.fglib.core.obj.stats

import com.uzery.fglib.utils.math.geom.PointN

class Stats {
    var fly = false

    var POS = PointN.ZERO
    var lPOS = PointN.ZERO
    var nPOS = PointN.ZERO

    var roomPOS = PointN.ZERO
    var sortPOS = PointN.ZERO

    val realPOS
        get() = roomPOS+POS

    var SIZE = PointN.ZERO
    var ALPHA = 0.0
}
