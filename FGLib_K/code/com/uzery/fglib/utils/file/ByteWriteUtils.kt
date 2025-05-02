package com.uzery.fglib.utils.file

import java.io.File
import java.io.FileOutputStream

object ByteWriteUtils {
    fun readFrom(input_path: String): ByteArray {
        return File(input_path).readBytes()
    }

    fun writeTo(bytes: ByteArray, output_path: String) {
        FileOutputStream(output_path).use { output ->
            output.write(bytes)
        }
    }
}
