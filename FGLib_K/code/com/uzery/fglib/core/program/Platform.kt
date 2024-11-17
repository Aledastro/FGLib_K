package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.data.FGCursor
import com.uzery.fglib.core.program.extension.Extension
import com.uzery.fglib.core.program.launch.LaunchOptions
import com.uzery.fglib.core.program.launch.PlatformSetup.realisation
import com.uzery.fglib.utils.graphics.RenderCamera
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.min
import kotlin.math.sign

object Platform {
    var MANUAL_CLEAR_ON = false
    var CLEAR_COLOR = FGColor.WHITE

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

    val clipboard = realisation.program.clipboard
    var clipboard_value
        get() = clipboard.string
        set(value) {
            clipboard.string = value
        }

    var scale: Int = 1
        get() {
            return if (field == -1) {
                val size = WINDOW/CANVAS
                min(size.intX, size.intY)
            } else field
        }

    var cursor = FGCursor.DEFAULT
        set(value) {
            field = value
            program.setCursor(cursor)
        }

    var options: LaunchOptions = LaunchOptions.default
    lateinit var ets: Array<out Extension>

    var render_camera = object: RenderCamera {
        override fun get(p: PointN): PointN {
            return PointN(p.X, p.Y)
        }

        override fun sort(p1: PointN, p2: PointN): Int {
            return (p1.Y-p2.Y).sign.toInt()
        }
    }

    fun exit() {
        program.exit()
    }

    val charArray
        get() = Array(Char.MAX_VALUE.code) { i -> Char(i) }

    private val updatables = HashSet<PlatformUpdatable>()
    fun addUpdatable(vararg updatable: PlatformUpdatable) {
        updatables.addAll(updatable)
    }

    internal fun update() {
        realisation.update()

        updatables.forEach { it.update() }
    }

    var develop_mode = false

    fun initWith(options: LaunchOptions, vararg ets: Extension) {
        Platform.options = options
        this.ets = ets
    }

    val WINDOW
        get() = PointN(program.WINDOW_SIZE)
    val CANVAS
        get() = PointN(options.size)
    val CANVAS_REAL
        get() = CANVAS*scale

    val WINDOW_R
        get() = RectN(PointN.ZERO, WINDOW)
    val CANVAS_R
        get() = RectN(PointN.ZERO, CANVAS)
    val CANVAS_REAL_R
        get() = RectN(PointN.ZERO, CANVAS_REAL)
}
