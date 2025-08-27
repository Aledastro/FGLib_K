package com.uzery.fglib.utils.file

import com.uzery.fglib.utils.BufferedIOUtils.getReader
import com.uzery.fglib.utils.BufferedIOUtils.getWriter
import com.uzery.fglib.utils.data.debug.DebugData
import java.io.File
import java.io.IOException

/**
 * TODO("doc")
 **/
object FileUtils {
    val SEPARATOR = '/'

    fun read(filename: String): String {
        getReader(filename).use { r ->
            return r.readText()
        }
    }

    fun readLines(filename: String): ArrayList<String> {
        return ArrayList(read(filename).split(Regex("\\R")))
    }

    fun write(filename: String, write: String, create_dirs: Boolean = false) {
        if (create_dirs) {
            val path = filename.substringBeforeLast(SEPARATOR)
            val pathDirs = path.split(SEPARATOR)

            var current = ""
            for (dir in pathDirs) {
                current += "$dir$SEPARATOR"
                val file = File(current)
                if (!file.exists()) file.mkdir()
            }
        }

        val wr = getWriter(filename)
        try {
            wr.write(write)
            wr.close()
        } catch (e: IOException) {
            throw DebugData.error("[ERROR] FilesData in write(): filename=$filename")
        }
    }

    fun existFile(filename: String): Boolean {
        return File(filename).exists()
    }

    fun removeFile(filename: String) {
        File(filename).delete()
    }

    fun normalizePath(path: String): String {
        val roots = File.listRoots().map { it.absolutePath }
        for (root in roots) {
            if (root.startsWith(path, ignoreCase = true)) {
                return root
            }
        }
        return path
    }

    fun filesFrom(filename: String): List<String> {
        return File(normalizePath(filename)).listFiles()
            ?.filter { it.isFile }
            ?.map { it.absolutePath.replace("\\", "/") }
            ?: emptyList()
    }

    fun dirsFrom(filename: String): List<String> {
        return File(normalizePath(filename)).listFiles()
            ?.filter { it.isDirectory }
            ?.map { it.absolutePath.replace("\\", "/") }
            ?: emptyList()
    }

    ///////////////////////////////////////////////////////////////////
}
