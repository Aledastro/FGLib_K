package com.uzery.fglib.core.program

import com.uzery.fglib.javafx_rl.GraphicsFX
import com.uzery.fglib.javafx_rl.JavaFXRealisation
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.input.KeyActivator
import com.uzery.fglib.utils.input.MouseActivator
import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import com.uzery.fglib.utils.math.num.IntI
import com.uzery.fglib.utils.program.FGCursor
import kotlin.math.min

object Platform {
    private val realisation = JavaFXRealisation
    private val program = realisation.program
    private val gc = realisation.graphics

    val graphics_ut = realisation.graphics
    val graphics = GraphicsFX.graphics

    val keyboard = realisation.listener.keyboard
    val char_keyboard = realisation.listener.char_keyboard
    val mouse = realisation.listener.mouse

    var scale: Int = 1
        get() = gc.scale
        set(value) {
            gc.scale = value
            field = value
        }
    var cursor = FGCursor.DEFAULT
        set(value) {
            field = value
            program.setCursor(cursor)
        }
    
    var options: LaunchOptions = LaunchOptions.default
        get(){
            return field
        }
    fun exit() {
        program.exit()
    }

    val charArray
        get() = Array(Char.MAX_VALUE.code) { i -> Char(i) }

    internal fun update() {
        keyboard.update()
        char_keyboard.update()
        mouse.keys.update()
    }

    var develop_mode = false

    fun resizeCursorFrom(pos: IntI): FGCursor {
        return when (pos) {
            IntI(-1, -1) -> FGCursor.NW_RESIZE
            IntI(0, -1) -> FGCursor.N_RESIZE
            IntI(1, -1) -> FGCursor.NE_RESIZE

            IntI(-1, 0) -> FGCursor.W_RESIZE
            IntI(0, 0) -> FGCursor.DEFAULT
            IntI(1, 0) -> FGCursor.E_RESIZE

            IntI(-1, 1) -> FGCursor.SW_RESIZE
            IntI(0, 1) -> FGCursor.S_RESIZE
            IntI(1, 1) -> FGCursor.SE_RESIZE

            else -> throw DebugData.error("wrong: $pos")
        }
    }

    fun init() {

    }

    val WINDOW
        get() = PointN(program.WINDOW_SIZE)
    val CANVAS
        get() = PointN(options.size)
    val CANVAS_REAL
        get() = CANVAS*gc.scale

    val WINDOW_R
        get() = RectN(PointN.ZERO, WINDOW)
    val CANVAS_R
        get() = RectN(PointN.ZERO, CANVAS)
    val CANVAS_REAL_R
        get() = RectN(PointN.ZERO, CANVAS_REAL)
}
