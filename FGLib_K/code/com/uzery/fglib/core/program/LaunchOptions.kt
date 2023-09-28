package com.uzery.fglib.core.program

import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.image.Image
import javafx.stage.StageStyle
import java.util.*

data class LaunchOptions(
    val size: PointN,
    val fullscreen: Boolean = false,
    val style: StageStyle = StageStyle.UNDECORATED,
    var title: String = "",
    var icons: List<String> = LinkedList(),
) {
    companion object {
        val default = LaunchOptions(PointN(700, 700))
    }
}
