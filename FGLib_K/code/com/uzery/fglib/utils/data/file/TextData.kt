package com.uzery.fglib.utils.data.file

import com.uzery.fglib.utils.BufferedIOUtils.getReader
import com.uzery.fglib.utils.BufferedIOUtils.getWriter
import com.uzery.fglib.utils.data.CollectDataClass
import com.uzery.fglib.utils.data.debug.DebugData
import java.io.File
import java.io.IOException

/**
 * TODO("doc")
 **/
object TextData: CollectDataClass() {
    val separator: String = File.separator

    operator fun get(filename: String) = readLines(filename)

    fun read(filename: String): String {
        getReader(resolvePath(filename)).use { r ->
            return r.readText()
        }
    }

    fun readLines(filename: String): ArrayList<String> {
        return getReader(resolvePath(filename)).useLines { sequence ->
            sequence.toCollection(ArrayList())
        }
    }

    fun write(filename: String, write: String, create_dirs: Boolean = false) {
        if (create_dirs) {
            val s = resolvePath(filename)
            val path = s.substring(0, s.lastIndexOf(separator))
            val pathDirs = path.split(separator)

            var current = ""
            for (dir in pathDirs) {
                current += "$dir$separator"
                val file = File(current)
                if (!file.exists()) file.mkdir()
            }
        }

        val wr = getWriter(resolvePath(filename))
        try {
            wr.write(write)
            wr.close()
        } catch (e: IOException) {
            throw DebugData.error("[ERROR] FilesData in write(): filename=$filename")
        }
    }

    fun existFile(filename: String): Boolean {
        return File(resolvePath(filename)).exists()
    }

    fun removeFile(filename: String) {
        File(resolvePath(filename)).delete()
    }

    fun filesFrom(filename: String): ArrayList<String> {
        val files = File(resolvePath(filename)).listFiles()
        val res = ArrayList<String>()
        files?.forEach { if (it.isFile) res.add(filename+"/"+it.name) }
        return res
    }

    fun dirsFrom(filename: String): ArrayList<String> {
        val dirs = File(resolvePath(filename)).listFiles()
        val res = ArrayList<String>()
        dirs?.forEach { if (it.isDirectory) res.add(filename+"/"+it.name) }
        return res
    }

    ///////////////////////////////////////////////////////////////////
}
