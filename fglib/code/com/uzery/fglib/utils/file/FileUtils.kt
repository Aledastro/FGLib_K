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

    operator fun get(filename: String) = readLines(filename)

    fun read(filename: String): String {
        getReader(filename).use { r ->
            return r.readText()
        }
    }

    fun readLines(filename: String): ArrayList<String> {
        return getReader(filename).useLines { sequence ->
            sequence.toCollection(ArrayList())
        }
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

    fun filesFrom(filename: String): List<String> {
        val files = File(filename).listFiles()
        val res = ArrayList<String>()
        files?.forEach { if (it.isFile) res.add(filename+"/"+it.name) }
        return res
    }

    fun dirsFrom(filename: String): List<String> {
        val dirs = File(filename).listFiles()
        val res = ArrayList<String>()
        dirs?.forEach { if (it.isDirectory) res.add(filename+"/"+it.name) }
        return res
    }

    ///////////////////////////////////////////////////////////////////
}
