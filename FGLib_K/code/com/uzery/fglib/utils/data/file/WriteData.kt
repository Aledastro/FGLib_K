package com.uzery.fglib.utils.data.file

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.ConstL.Companion.BUFFER_FORMAT
import com.uzery.fglib.utils.data.file.ConstL.Companion.RUN_JAR
import java.io.*
import java.util.stream.Collectors

interface WriteData {
    companion object {
        var directory = ""

        operator fun get(filename: String): ArrayList<String> {
            return getReader(resolvePath(filename)).lines().collect(Collectors.toCollection { ArrayList() })
        }

        fun write(filename: String, write: String) {
            val wr = getWriter("${directory}$filename")
            try {
                wr.write(write)
                wr.close()
            } catch (e: IOException) {
                throw DebugData.error("[ERROR] FilesData in write(): filename=$filename")
            }
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
                return WriteData::class.java.getResourceAsStream("/$filename")
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

        fun errorStream(): PrintStream {
            return PrintStream(outFileStream("C:/com.uzery.fglib.utils.data.image.Data/!errors.txt"))
        }

        private fun resolvePath(name: String): String {
            return "$directory$name".replace("/", File.separator)
        }

    }
}