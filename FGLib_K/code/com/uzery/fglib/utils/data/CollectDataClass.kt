package com.uzery.fglib.utils.data

/**
 * TODO("doc")
 **/
abstract class CollectDataClass {
    var paths = MediaPaths()

    var dir: String
        get() = paths.dir
        set(value) {
            paths.dir = value
        }

    internal fun resolvePath(name: String): String {
        return paths.resolvePath(name)
    }
}
