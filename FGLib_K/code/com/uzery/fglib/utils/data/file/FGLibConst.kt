package com.uzery.fglib.utils.data.file

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.struct.EnumVar

/**
 * TODO("doc")
 **/
object FGLibConst {
    const val LITTLE = 0.00_001

    val FGLIB_LIGHT_COLOR = FGColor.web("#dfe0e8")
    val FGLIB_DARK_COLOR = FGColor.web("#3a3f5e")

    const val BUFFER_FORMAT = "UTF-8"

    val FILES_COMMENT: String
        get() = "//Uzery Studio 2017-2025\n\n"

    val DEFAULT_SCALE
        get() = EnumVar(
            0.02, 0.03, 0.04, 0.05, 0.075, 0.1,
            0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.75,
            1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0
        ).also { it.id = it.fromValue(1.0) }
}
