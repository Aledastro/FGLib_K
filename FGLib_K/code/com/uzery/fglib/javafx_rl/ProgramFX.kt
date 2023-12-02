package com.uzery.fglib.javafx_rl

import com.uzery.fglib.core.program.*
import com.uzery.fglib.utils.data.image.ImageUtils
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.program.FGCursor
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCombination
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlin.math.max

internal object ProgramFX: FGProgram() {
    lateinit var stage: Stage

    override lateinit var WINDOW_SIZE: PointN

    lateinit var gc: GraphicsContext

    private var inited = false
    override fun setCursor(cursor: FGCursor) {
        if(!inited) return
        stage.scene.cursor = Cursor.cursor(cursor.name)
    }

    fun initWith(stage: Stage) {
        ProgramFX.stage = stage

        WINDOW_SIZE = PointN(Screen.getPrimary().bounds.width, Screen.getPrimary().bounds.height)

        val size = Platform.options.size*Platform.scale
        val canvas = Canvas(size.X, size.Y)

        gc = canvas.graphicsContext2D
        gc.isImageSmoothing = false
        stage.scene = Scene(Group(canvas))
        inited = true

        stage.initStyle(StageStyle.valueOf(Platform.options.style.name))
        stage.isFullScreen = Platform.options.fullscreen
        stage.fullScreenExitKeyCombination = KeyCombination.NO_MATCH
        Platform.options.icons.forEach { stage.icons.add(ImageUtils.from(it).source) }

        if (stage.isFullScreen) {
            val offset = (WINDOW_SIZE-size)/2
            canvas.layoutX = max(0.0, offset.X)
            canvas.layoutY = max(0.0, offset.Y)
        }

        setCursor(Platform.cursor)

        stage.scene.fill = FGColor.fromFGColor(Platform.options.fill)

        stage.title = Platform.options.title
        stage.show()
    }

    override fun exit() {
        stage.close()
    }
}
