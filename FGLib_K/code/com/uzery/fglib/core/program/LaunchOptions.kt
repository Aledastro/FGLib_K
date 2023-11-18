package com.uzery.fglib.core.program

import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.stage.StageStyle
import java.util.*

data class LaunchOptions(
    val size: PointN,
    val fullscreen: Boolean = false,
    val fill: Paint = Color.WHITE,
    var title: String = "",
    var icons: List<String> = ArrayList(),
    val style: StageStyle = StageStyle.UNDECORATED
) {
    companion object {
        val default = LaunchOptions(PointN(700, 700))
    }
}
