package com.uzery.fglib.core.obj

import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.utils.math.geom.PointN

/**
 * [GameObject] Transform Rules
 *
 * Can be used for manipulating in RoomEditor
 **/
abstract class ObjectTransform(val obj: GameObject) {
    open val name = ""
    open val vis: Visualiser? = null

    open val move: (d_pos: PointN) -> Unit = { d_pos ->
        pos += d_pos
    }
    open val resize: (d_size: PointN) -> Unit = { d_size ->
        size += d_size
    }
    open val resize_move: (d_size: PointN) -> Unit
        get() = this.move

    open val rotateTo: (a: Double) -> Unit = { a ->
        alpha = a
    }
    open val rotate_once: (() -> Unit)? = null
    open val flipH: (() -> Unit)? = null
    open val flipV: (() -> Unit)? = null

    open val group_rotate_to: ((a: Double) -> Unit)?
        get() = rotateTo
    open val group_rotate_once: (() -> Unit)?
        get() = rotate_once
    open val group_flipH: (() -> Unit)?
        get() = flipH
    open val group_flipV: (() -> Unit)?
        get() = flipV

    open val resize_turnX: Boolean = true
    open val resize_turnY: Boolean = true

    open val addIn: ((pos: PointN, size: PointN) -> Unit)? = null
    open val removeIn: ((pos: PointN, size: PointN) -> Unit)? = null

    var pos = PointN.ZERO
        get() = obj.stats.POS
        set(value) {
            obj.stats.POS = value
            field = value
        }
    var size = PointN.ZERO
        get() = obj.stats.SIZE
        set(value) {
            obj.stats.SIZE = value
            field = value
        }
    var alpha = 0.0
        get() = obj.stats.ALPHA
        set(value) {
            obj.stats.ALPHA = value
            field = value
        }


    open val show_pos: PointN
        get() = pos
    open val show_size: PointN
        get() = size
}
