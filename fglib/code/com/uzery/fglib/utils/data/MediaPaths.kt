package com.uzery.fglib.utils.data

import com.uzery.fglib.utils.FGUtils

/**
 * TODO("doc")
 **/
class MediaPaths {
    var dir = ""

    private val paths = HashMap<String, String>()

    //private val real_paths = HashMap<String, String>()

    //private var real_dir = ""

    fun resolvePath(name: String): String {
        var local_path = ""
        var last = name
        if ('|' in name) {
            local_path = paths[FGUtils.subBefore(name, "|")].orEmpty()
            last = FGUtils.subAfter(name, "|")
        }

        return "$dir$local_path$last"
    }

    operator fun set(key: String, value: String) {
        paths[key] = value
    }

    fun updatePaths() {
        /*paths.forEach { (code, value) ->
            real_paths[code] = value
        }
        paths.clear()

        real_dir = dir*/
    }

    override fun toString(): String {
        return buildString {
            append("media_paths[\n")
            append("\tdir: $dir\n")
            append("\n")
            for ((key, value) in paths) {
                append("\t$key = $value\n")
            }
            append("]")
        }
    }
}
