package com.uzery.fglib.utils

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.FGLibConst.BUFFER_FORMAT
import java.io.*

object BufferedIOUtils {
    fun getWriter(filename: String): BufferedWriter {
        return getWriter(outFileStream(filename))
    }

    fun getReader(filename: String): BufferedReader {
        return getReader(inFileStream(filename))
    }

    fun getWriter(stream: OutputStream): BufferedWriter {
        return try {
            BufferedWriter(OutputStreamWriter(stream, BUFFER_FORMAT))
        } catch (e: IOException) {
            throw DebugData.error("[ERROR] FilesData in getWriter(): stream=$stream")
        }
    }

    fun getReader(stream: InputStream): BufferedReader {
        return try {
            BufferedReader(InputStreamReader(stream, BUFFER_FORMAT))
        } catch (e: IOException) {
            throw DebugData.error("[ERROR] FilesData in getReader(): stream=$stream")
        }
    }

    fun inFileStream(filename: String): InputStream {
        return try {
            FileInputStream(File(filename).absolutePath)
        } catch (e: Exception) {
            throw DebugData.error("Can't run stream from: $filename")
        }
    }

    fun outFileStream(filename: String): OutputStream {
        return try {
            FileOutputStream(filename)
        } catch (e: FileNotFoundException) {
            throw DebugData.error("Can't run stream from: $filename")
        }
    }
}
