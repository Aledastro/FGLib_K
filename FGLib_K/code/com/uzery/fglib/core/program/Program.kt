package com.uzery.fglib.core.program

import com.uzery.fglib.utils.data.image.ImageUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.animation.AnimationTimer
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.stage.Screen
import javafx.stage.Stage
import kotlin.math.max

internal object Program {
    var cursor: Cursor? = null
        set(value) {
            field = value
            stage.scene.cursor = value
        }
    private lateinit var stage: Stage

    internal lateinit var gc: GraphicsContext

    internal val pressed = Array(KeyCode.values().size) { false }
    internal val mouse_pressed = Array(KeyCode.values().size) { false }
    internal var mouseP = PointN.ZERO
    internal var options = LaunchOptions.default

    lateinit var WINDOW_SIZE: PointN

    private val core = object: Extension() {}

    internal fun initWith(options: LaunchOptions, vararg ets: Extension) {
        core.children.addAll(ets)

        this.options = options
    }

    internal fun startWith(stage: Stage) {
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

        if(stage.isFullScreen){
            val offset = (WINDOW_SIZE - size)/2
            canvas.layoutX = max(0.0, offset.X)
            canvas.layoutY = max(0.0, offset.Y)
        }

        stage.scene.fill = options.fill

        stage.title = options.title
        stage.show()

        this.stage.scene.setOnMousePressed { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = true
        }
        this.stage.scene.setOnMouseReleased { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = false
        }
        this.stage.scene.setOnMouseMoved { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = false
        }
        this.stage.scene.setOnMouseDragged { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = true
        }
        this.stage.scene.setOnKeyPressed { key -> pressed[key.code.ordinal] = true }
        this.stage.scene.setOnKeyReleased { key -> pressed[key.code.ordinal] = false }

        core.initWithChildren()

        val timer = object: AnimationTimer() {
            override fun handle(t: Long) {
                core.updateTasksWithChildren()
                core.updateWithChildren()
                core.drawWithChildren(core.draw_pos)

                Platform.update()
            }
        }

        timer.start()
    }

    fun close() {
        stage.close()
    }
}
