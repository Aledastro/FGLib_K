package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.PlatformSetup.realisation
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import com.uzery.fglib.utils.math.num.IntI
import com.uzery.fglib.utils.program.FGCursor

object Platform {
    private val program
        get() = realisation.program
    val graphics
        get() = realisation.graphics
    val packager
        get() = realisation.packager

    val keyboard
        get() = realisation.listener.keyboard
    val char_keyboard
        get() = realisation.listener.char_keyboard
    val mouse
        get() = realisation.listener.mouse

    var scale: Int = 1
        get() = graphics.scale
        set(value) {
            graphics.scale = value
            field = value
        }
    var cursor = FGCursor.DEFAULT
        set(value) {
            field = value
            program.setCursor(cursor)
        }
    
    var options: LaunchOptions = LaunchOptions.default
        get(){
            if(field == null) return LaunchOptions.default
            return field
        }
    lateinit var ets: Array<out Extension>

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

    fun initWith(options: LaunchOptions, vararg ets: Extension) {
        Platform.options = options
        this.ets = ets
    }

    val WINDOW
        get() = PointN(program.WINDOW_SIZE)
    val CANVAS
        get() = PointN(options.size)
    val CANVAS_REAL
        get() = CANVAS*graphics.scale

    val WINDOW_R
        get() = RectN(PointN.ZERO, WINDOW)
    val CANVAS_R
        get() = RectN(PointN.ZERO, CANVAS)
    val CANVAS_REAL_R
        get() = RectN(PointN.ZERO, CANVAS_REAL)
}
