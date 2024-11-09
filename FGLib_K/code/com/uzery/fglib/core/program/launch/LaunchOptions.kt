package com.uzery.fglib.core.program.launch

import com.uzery.fglib.core.program.data.FGWindowMode
import com.uzery.fglib.core.program.data.FGWindowStyle
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

data class LaunchOptions(
    val size: PointN,
    val window_mode: FGWindowMode = FGWindowMode.WINDOWED,
    val fill: FGColor = FGColor.WHITE,
    var title: String = "",
    var icons: List<String> = ArrayList(),
    val style: FGWindowStyle = FGWindowStyle.UNDECORATED
) {
    companion object {
        val default = LaunchOptions(PointN(700, 700))
        val default2 = LaunchOptions(
            PointN(1920, 1080)/2,
            window_mode = FGWindowMode.FULLSCREEN,
            FGColor.WHITE,
            "FGLib Example",
            listOf("sys|fglib_icon.png"),
            FGWindowStyle.UNDECORATED
        )
    }
}
