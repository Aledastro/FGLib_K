package com.uzery.fglib.utils.data.file

import com.uzery.fglib.utils.data.CollectDataClass
import com.uzery.fglib.utils.file.FileUtils

/**
 * TODO("doc")
 **/
object TextData: CollectDataClass() {
    val SEPARATOR: String = FileUtils.SEPARATOR

    operator fun get(filename: String) = readLines(resolvePath(filename))

    fun read(filename: String) = FileUtils.read(resolvePath(filename))
    fun readLines(filename: String) = FileUtils.readLines(resolvePath(filename))

    fun write(filename: String, write: String, create_dirs: Boolean = false) {
        FileUtils.write(resolvePath(filename), write, create_dirs)
    }

    fun existFile(filename: String) = FileUtils.existFile(resolvePath(filename))
    fun removeFile(filename: String) = FileUtils.removeFile(resolvePath(filename))

    fun filesFrom(filename: String): List<String> {
        return FileUtils.filesFrom(resolvePath(filename))
            .map { it.substring(dir.length) }
    }

    fun dirsFrom(filename: String): List<String> {
        return FileUtils.dirsFrom(resolvePath(filename))
            .map { it.substring(dir.length) }
    }

    ///////////////////////////////////////////////////////////////////
}
