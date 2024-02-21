package com.uzery.fglib.core.program

import com.uzery.fglib.utils.math.num.IntI

enum class FGLayout(id: IntI) {
    TOP_LEFT(IntI(-1, -1)),
    TOP(IntI(0, -1)),
    TOP_RIGHT(IntI(1, -1)),

    LEFT(IntI(-1, 0)),
    CENTER(IntI(0, 0)),
    RIGHT(IntI(1, 0)),

    BOTTOM_LEFT(IntI(-1, 1)),
    BOTTOM(IntI(0, 1)),
    BOTTOM_RIGHT(IntI(1, 1)),
}
