package com.uzery.fglib.utils.data.file

import com.uzery.fglib.utils.math.FGUtils

object ConstL {
    var RUN_JAR = false
    const val LITTLE = 0.00_001

    const val BUFFER_FORMAT = "UTF-8"

    val FILES_COMMENT: String
        get() = "//Uzery Studio 2017-2024\n"+
                "//last edit: ${FGUtils.time_YMD()} ${FGUtils.time_HMS()}\n\n"
}
