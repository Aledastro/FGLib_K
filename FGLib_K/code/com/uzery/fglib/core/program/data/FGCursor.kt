package com.uzery.fglib.core.program.data

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
enum class FGCursor {
    DEFAULT,
    CROSSHAIR,
    TEXT,
    WAIT,
    SW_RESIZE,
    SE_RESIZE,
    NW_RESIZE,
    NE_RESIZE,
    N_RESIZE,
    S_RESIZE,
    W_RESIZE,
    E_RESIZE,
    OPEN_HAND,
    CLOSED_HAND,
    HAND,
    MOVE,
    DISAPPEAR,
    H_RESIZE,
    V_RESIZE,
    NONE;

    companion object {
        fun resize(pos: IntI): FGCursor {
            return when (pos) {
                IntI(-1, -1) -> NW_RESIZE
                IntI(0, -1) -> N_RESIZE
                IntI(1, -1) -> NE_RESIZE

                IntI(-1, 0) -> W_RESIZE
                IntI(0, 0) -> DEFAULT
                IntI(1, 0) -> E_RESIZE

                IntI(-1, 1) -> SW_RESIZE
                IntI(0, 1) -> S_RESIZE
                IntI(1, 1) -> SE_RESIZE

                else -> throw DebugData.error("wrong: $pos")
            }
        }
    }
}
