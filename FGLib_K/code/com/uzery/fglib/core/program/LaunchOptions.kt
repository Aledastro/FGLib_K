package com.uzery.fglib.core.program

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

data class LaunchOptions(
    val size: PointN,
    val fullscreen: Boolean = false,
    val fill: FGColor = FGColor.WHITE,
    var title: String = "",
    var icons: List<String> = ArrayList(),
    val style: FGWindowStyle = FGWindowStyle.UNDECORATED
) {
    companion object {
        val default = LaunchOptions(PointN(700, 700))
        val default2 = LaunchOptions(
            PointN(1920, 1080)/2,
            fullscreen = true,
            FGColor.WHITE,
            "FGLib Example",
            listOf("sys|fglib_icon.png"),
            FGWindowStyle.UNDECORATED
        )
    }
}
