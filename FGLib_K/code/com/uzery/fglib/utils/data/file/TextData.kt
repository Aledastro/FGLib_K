package com.uzery.fglib.utils.data.file

import com.uzery.fglib.utils.data.CollectDataClass
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.ConstL.BUFFER_FORMAT
import com.uzery.fglib.utils.data.file.ConstL.RUN_JAR
import java.io.*
import java.util.stream.Collectors

object TextData: CollectDataClass() {
    operator fun get(filename: String): ArrayList<String> {
        val rd = getReader(resolvePath(filename))
        val lines = rd.lines().collect(Collectors.toCollection { ArrayList() })
        rd.close()
        return lines
    }

    fun write(filename: String, write: String, create_dirs: Boolean = false) {
        if (create_dirs){
            val s = resolvePath(filename)
            val file = File(s.substring(0, s.lastIndexOf("\\")))
            file.mkdir()
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

    fun removeFile(filename: String){
        File(resolvePath(filename)).delete()
    }

    ///////////////////////////////////////////////////////////////////
    private fun getWriter(filename: String): BufferedWriter {
        return getWriter(outFileStream(filename))
    }

    private fun getReader(filename: String): BufferedReader {
        return getReader(inFileStream(filename))
    }

    private fun getWriter(stream: OutputStream): BufferedWriter {
        return try {
            BufferedWriter(OutputStreamWriter(stream, BUFFER_FORMAT))
        } catch (e: IOException) {
            throw DebugData.error("[ERROR] FilesData in getWriter(): stream=$stream")
        }
    }

    private fun getReader(stream: InputStream): BufferedReader {
        return try {
            BufferedReader(InputStreamReader(stream, BUFFER_FORMAT))
        } catch (e: IOException) {
            throw DebugData.error("[ERROR] FilesData in getReader(): stream=$stream")
        }
    }

    private fun inFileStream(filename: String): InputStream {
        return if (RUN_JAR) {
            return TextData::class.java.getResourceAsStream("/$filename")
                ?: throw DebugData.error("Can't run stream from: $filename")
        } else {
            try {
                FileInputStream(File(filename).absolutePath)
            } catch (e: Exception) {
                throw DebugData.error("Can't run stream from: $filename")
            }
        }
    }

    private fun outFileStream(filename: String): OutputStream {
        return try {
            FileOutputStream(filename)
        } catch (e: FileNotFoundException) {
            throw DebugData.error("Can't run stream from: $filename")
        }
    }

    private fun outFileStreamX(filename: String): OutputStream {
        return if (RUN_JAR) {
            try {
                FileOutputStream("/$filename")
            } catch (e: FileNotFoundException) {
                throw DebugData.error("Can't run stream from: $filename")
            }
        } else {
            try {
                FileOutputStream("src/$filename")
            } catch (e: IOException) {
                throw DebugData.error("Can't run stream from: $filename")
            }
        }
    }
}
