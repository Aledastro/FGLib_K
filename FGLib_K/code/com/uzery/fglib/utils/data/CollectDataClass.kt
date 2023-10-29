package com.uzery.fglib.utils.data

import com.uzery.fglib.utils.math.FGUtils
import java.io.File

abstract class CollectDataClass {
    val paths = HashMap<String, String>()
    var dir = ""

    internal fun resolvePath(name: String): String {
        var local_path = ""
        var last = name
        if (name.contains('|')) {
            local_path = paths[FGUtils.subBefore(name, "|")].orEmpty()
            last = FGUtils.subAfter(name, "|")
        }

        return "$dir$local_path$last".replace("/", File.separator)
    }
}
