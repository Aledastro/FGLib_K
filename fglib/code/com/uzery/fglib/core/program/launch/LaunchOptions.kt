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
)
