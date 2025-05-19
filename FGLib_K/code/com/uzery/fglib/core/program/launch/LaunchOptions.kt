package com.uzery.fglib.core.program.launch

import com.uzery.fglib.core.program.data.FGWindowMode
import com.uzery.fglib.core.program.data.FGWindowStyle
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
data class LaunchOptions(
    val size: PointN,
    val window_mode: FGWindowMode = FGWindowMode.WINDOWED,
    val fill: FGColor = FGColor.PURE_WHITE,
    var title: String = "",
    var icons: List<String> = ArrayList(),
    val style: FGWindowStyle = FGWindowStyle.UNDECORATED
) {
    companion object {
        val default = LaunchOptions(PointN(700, 700))
        val default2 = LaunchOptions(
            PointN(1920, 1080)/2,
            window_mode = FGWindowMode.FULLSCREEN,
            FGColor.PURE_WHITE,
            "FGLib Example",
            listOf("sys|fglib_icon.png"),
            FGWindowStyle.UNDECORATED
        )
    }

    constructor(
        size: PointN,
        fullscreen: Boolean,
        fill: FGColor = FGColor.PURE_WHITE,
        title: String = "",
        icons: List<String> = ArrayList(),
        style: FGWindowStyle = FGWindowStyle.UNDECORATED
    ): this(size, if (fullscreen) FGWindowMode.FULLSCREEN else FGWindowMode.WINDOWED, fill, title, icons, style)
}
