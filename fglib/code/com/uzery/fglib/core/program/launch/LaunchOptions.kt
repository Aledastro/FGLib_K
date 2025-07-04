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
    val style: FGWindowStyle = FGWindowStyle.UNDECORATED,
    var support_tray: Boolean = false,
    var tray_icon: String = ""
) {
    companion object {
        val default = LaunchOptions(PointN(700, 700))
        val default2 = LaunchOptions(
            size = PointN(1920, 1080)/2,
            window_mode = FGWindowMode.FULLSCREEN,
            fill = FGColor.PURE_WHITE,
            title = "FGLib Example",
            icons = listOf("sys|fglib_icon.png"),
            style = FGWindowStyle.UNDECORATED,
            support_tray = false
        )
    }

    constructor(
        size: PointN,
        fullscreen: Boolean,
        fill: FGColor = FGColor.PURE_WHITE,
        title: String = "",
        icons: List<String> = ArrayList(),
        style: FGWindowStyle = FGWindowStyle.UNDECORATED,
        support_tray: Boolean = false,
        tray_icon: String = ""
    ): this(
        size,
        if (fullscreen) FGWindowMode.FULLSCREEN else FGWindowMode.WINDOWED,
        fill,
        title,
        icons,
        style,
        support_tray,
        tray_icon
    )
}
