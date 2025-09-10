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

    ////////////////////////////////////////////////////////////////////////////////////

    fun read(filename: String): String {
        getReader(filename).use { r ->
            return r.readText()
        }
    }

    fun readLines(filename: String): ArrayList<String> {
        return ArrayList(read(filename).split(Regex("\\R")))
    }

    fun write(filename: String, content: String, create_dirs: Boolean = false) {
        if (create_dirs) {
            getFile(filename).parentFile?.mkdirs()
        }

        try {
            getWriter(filename).use { writer ->
                writer.write(content)
            }
        } catch (e: IOException) {
            throw DebugData.error("[ERROR] FilesData in write(): filename=$filename")
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    fun existsFile(filename: String): Boolean {
        return getFile(filename).exists()
    }

    fun removeFile(filename: String) {
        getFile(filename).delete()
    }

    ////////////////////////////////////////////////////////////////////////////////////

    fun walk(filename: String, f: (String) -> Unit) {
        for (file in getFile(filename).walkTopDown()) {
            f(file.invariantSeparatorsPath)
        }
    }

    fun walkSequence(filename: String): Sequence<String> {
        return getFile(filename).walkTopDown().map { it.invariantSeparatorsPath }
    }

    fun filesFrom(filename: String): List<String> {
        return getFile(filename).listFiles()
            ?.filter { it.isFile }
            ?.map { it.invariantSeparatorsPath }
            ?: emptyList()
    }

    fun dirsFrom(filename: String): List<String> {
        return getFile(filename).listFiles()
            ?.filter { it.isDirectory }
            ?.map { it.invariantSeparatorsPath }
            ?: emptyList()
    }

    ////////////////////////////////////////////////////////////////////////////////////

    private fun getFile(filename: String) = File(normalizePath(filename))

    private fun normalizePath(path: String): String {
        val roots = File.listRoots().map { it.invariantSeparatorsPath }
        for (root in roots) {
            if (root.startsWith(path, ignoreCase = true)) {
                return root
            }
        }
        return path
    }

    ////////////////////////////////////////////////////////////////////////////////////
}
