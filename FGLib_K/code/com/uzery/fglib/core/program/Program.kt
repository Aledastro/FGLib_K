package com.uzery.fglib.core.program

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.image.ImageUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.animation.AnimationTimer
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.stage.Screen
import javafx.stage.Stage
import kotlin.math.max

internal object Program {
    private lateinit var stage: Stage

    lateinit var gc: GraphicsContext

    val pressed = Array(KeyCode.values().size) { false }
    private val char_pressed = Array(Platform.charArray.size) { -1 }
    val char_pressedID = HashMap<Char, Int>()
    val mouse_pressed = Array(KeyCode.values().size) { false }
    var mouseP = PointN.ZERO


    fun char_pressed(code: Int): Boolean {
        return char_pressed[code] == program_time
    }

    var scrollP = PointN.ZERO
        get() = if (last_scroll_time == program_time) field else PointN.ZERO
    private var last_scroll_time = 0
    var options = LaunchOptions.default

    lateinit var WINDOW_SIZE: PointN

    private val core = object: Extension() {}

    fun initWith(options: LaunchOptions, vararg ets: Extension) {
        core.children.addAll(ets)

        this.options = options
    }

    private var program_time = 0
    fun startWith(stage: Stage) {
        this.stage = stage

        WINDOW_SIZE = PointN(Screen.getPrimary().bounds.width, Screen.getPrimary().bounds.height)

        val size = options.size*Platform.scale
        val canvas = Canvas(size.X, size.Y)

        gc = canvas.graphicsContext2D
        gc.isImageSmoothing = false
        stage.scene = Scene(Group(canvas))
        stage.initStyle(options.style)
        stage.isFullScreen = options.fullscreen
        stage.fullScreenExitKeyCombination = KeyCombination.NO_MATCH
        options.icons.forEach { stage.icons.add(ImageUtils.from(it)) }

        if (stage.isFullScreen) {
            val offset = (WINDOW_SIZE-size)/2
            canvas.layoutX = max(0.0, offset.X)
            canvas.layoutY = max(0.0, offset.Y)
        }

        setCursor()

        stage.scene.fill = options.fill

        stage.title = options.title
        stage.show()

        stage.scene.setOnMousePressed { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = true
        }
        stage.scene.setOnMouseReleased { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = false
        }
        stage.scene.setOnMouseMoved { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = false
        }
        stage.scene.setOnMouseDragged { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = true
        }
        stage.scene.setOnScroll { e ->
            scrollP = PointN(e.deltaX, e.deltaY)
            last_scroll_time = program_time
        }

        Platform.charArray.forEachIndexed { i, value -> char_pressedID[value] = i }
        stage.scene.setOnKeyPressed { key -> pressed[key.code.ordinal] = true }
        stage.scene.setOnKeyReleased { key -> pressed[key.code.ordinal] = false }

        stage.scene.setOnKeyTyped { key ->
            key.character.forEach { ch->
                if(char_pressedID[ch] == null) throw DebugData.error("type key: $key, char: ${key.character}")
                char_pressed[char_pressedID[ch]!!] = program_time
            }
        }

        core.initWithChildren()

        val timer = object: AnimationTimer() {
            override fun handle(t: Long) {
                core.updateTasksWithChildren()
                core.updateWithChildren()
                core.drawWithChildren(core.draw_pos)

                Platform.update()
                program_time++
            }
        }

        timer.start()

        inited = true
    }

    fun close() {
        stage.close()
    }

    fun setCursor() {
        stage.scene.cursor = Platform.cursor ?: Cursor.DEFAULT
    }

    var inited = false
        private set
}
