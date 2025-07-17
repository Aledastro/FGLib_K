package com.uzery.fglib.core.program.launch

import com.uzery.fglib.core.program.data.FGWindowMode
import com.uzery.fglib.core.program.data.FGWindowStyle
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
data class LaunchOptions(
    val canvas_size: PointN,
    val resize_method: ResizeMethod,
    val window_mode: FGWindowMode = FGWindowMode.WINDOWED,
    val fill: FGColor = FGColor.PURE_WHITE,
    var title: String = "",
    var icons: List<String> = ArrayList(),
    val style: FGWindowStyle = FGWindowStyle.UNDECORATED,
    var resizable: Boolean = false,
    var tray: TraySupport? = null
) {
    companion object {
        val default = LaunchOptions(
            canvas_size = PointN(700, 700),
            resize_method = ResizeMethod.PIXEL_PERFECT(2)
        )
        val default2 = LaunchOptions(
            canvas_size = PointN(1920, 1080)/2,
            resize_method = ResizeMethod.PIXEL_PERFECT(2),
            window_mode = FGWindowMode.FULLSCREEN,
            fill = FGColor.PURE_WHITE,
            title = "FGLib Example",
            icons = listOf("sys|fglib_icon.png"),
            style = FGWindowStyle.UNDECORATED,
            resizable = false,
            tray = null
        )
    }
}
