package com.uzery.fglib.core.program

import com.uzery.fglib.utils.math.geom.PointN
import javafx.stage.StageStyle

data class LaunchOptions(val size: PointN, val style: StageStyle = StageStyle.UNDECORATED) {
    companion object {
        val default = LaunchOptions(PointN(700.0, 700.0))
    }
}
