package com.uzery.fglib.core.program

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN
import javafx.stage.StageStyle

data class LaunchOptions(
    val size: PointN,
    val fullscreen: Boolean = false,
    val fill: FGColor = FGColor.WHITE,
    var title: String = "",
    var icons: List<String> = ArrayList(),
    val style: StageStyle = StageStyle.UNDECORATED
) {
    companion object {
        val default = LaunchOptions(PointN(700, 700))
    }
}
