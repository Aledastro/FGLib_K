package com.uzery.fglib.core.obj.stats

import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
class Stats {
    var fly = false
    val fly_by = HashMap<Int, Boolean>()
    var sticky = false
    val sticky_by = HashMap<Int, Boolean>()

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
