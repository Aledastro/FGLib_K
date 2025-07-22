package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.data.FGCursor
import com.uzery.fglib.core.program.extension.Extension
import com.uzery.fglib.core.program.launch.LaunchOptions
import com.uzery.fglib.core.program.launch.PlatformSetup.realisation
import com.uzery.fglib.utils.graphics.RenderCamera
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.sign

/**
 * TODO("doc")
 **/
object Platform {
    ////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////

    var cursor = FGCursor.DEFAULT
        set(value) {
            field = value
            program.setCursor(cursor)
        }

    ////////////////////////////////////////////////////////////////////////////////////////

    lateinit var options: LaunchOptions

    var render_camera = object: RenderCamera {
        override fun get(p: PointN): PointN {
            return PointN(p.X, p.Y)
        }

        override fun sort(p1: PointN, p2: PointN): Int {
            return (p1.Y-p2.Y).sign.toInt()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    val window
        get() = program.window

    ////////////////////////////////////////////////////////////////////////////////////////

    var on_exit: () -> Unit = {}
    var on_event_exit: () -> Unit = {
        exit()
    }

    fun exit() {
        on_exit()
        program.exit()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun reRender() {
        program.reRender()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    var extension_focused: Extension? = null
        internal set
    var extension_at_top: Extension? = null
        internal set

    fun getGlobalAtTop(pos: PointN) = Program.core.getAtTop(pos)

    ////////////////////////////////////////////////////////////////////////////////////////

    val charArray
        get() = Array(Char.MAX_VALUE.code) { i -> Char(i) }

    ////////////////////////////////////////////////////////////////////////////////////////

    private val updatables = HashSet<PlatformUpdatable>()
    fun addUpdatable(vararg updatable: PlatformUpdatable) {
        updatables.addAll(updatable)
    }

    internal fun update() {
        realisation.update()

        updatables.forEach { it.update() }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    var develop_mode = false

    ////////////////////////////////////////////////////////////////////////////////////////

    val SCREEN
        get() = PointN(program.SCREEN_SIZE)
    val VISUAL_BOUNDS
        get() = PointN(program.VISUAL_BOUNDS)
    val WINDOW
        get() = PointN(window.size)
    val CANVAS
        get() = options.resize_method.adapt(options.canvas_size)

    val SCREEN_R
        get() = RectN(PointN.ZERO, SCREEN)
    val VISUAL_BOUNDS_R
        get() = RectN(PointN.ZERO, VISUAL_BOUNDS)
    val WINDOW_R
        get() = RectN(PointN.ZERO, WINDOW)
    val CANVAS_R
        get() = RectN(PointN.ZERO, CANVAS)

    ////////////////////////////////////////////////////////////////////////////////////////
}
