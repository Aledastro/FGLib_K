package com.uzery.fglib.utils.data

import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.FGUtils

abstract class CollectDataClass {
    val paths = HashMap<String, String>()

    //private val real_paths = HashMap<String, String>()
    var dir = ""
    //private var real_dir = ""

    internal fun resolvePath(name: String): String {
        var local_path = ""
        var last = name
        if ('|' in name) {
            local_path = paths[FGUtils.subBefore(name, "|")].orEmpty()
            last = FGUtils.subAfter(name, "|")
        }

        return "$dir$local_path$last".replace("/", TextData.separator)
    }

    fun updatePaths() {
        /*paths.forEach { (code, value) ->
            real_paths[code] = value
        }
        paths.clear()

        real_dir = dir*/
    }
}
