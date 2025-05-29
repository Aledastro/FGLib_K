package com.uzery.fglib.utils.file

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ZipUtils {
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun zip(
        input_path: String,
        output_path: String,
        flatten: Boolean = false,
        filter: (String) -> Boolean = { true }
    ) {
        ZipOutputStream(FileOutputStream(output_path)).use { zip_out ->
            val file = File(input_path)
            zipFile(file, basePath(file, flatten), zip_out, filter)
        }
    }

    fun zipToBytes(
        input_path: String,
        flatten: Boolean = false,
        filter: (String) -> Boolean = { true }
    ): ByteArray {
        val file = File(input_path)

        val byte_stream = ByteArrayOutputStream()
        ZipOutputStream(byte_stream).use { zip_out ->
            zipFile(file, basePath(file, flatten), zip_out, filter)
        }
        return byte_stream.toByteArray()
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun zipFile(
        file: File,
        filename: String,
        zip_out: ZipOutputStream,
        filter: (String) -> Boolean
    ) {
        if (!filter(filename)) return

        if (file.isDirectory) {
            val dir_name = if (filename.endsWith("/")) filename else "$filename/"
            zip_out.putNextEntry(ZipEntry(dir_name))
            zip_out.closeEntry()

            val children = file.listFiles() ?: return
            for (child_file in children) {
                zipFile(child_file, filename+"/"+child_file.name, zip_out, filter)
            }
        } else {
            FileInputStream(file).use { input ->
                zip_out.putNextEntry(ZipEntry(filename))
                input.copyTo(zip_out)
                zip_out.closeEntry()
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun unzip(
        input_file: String,
        output_path: String,
        filter: (String) -> Boolean = { true }
    ) {
        FileInputStream(input_file).use { stream ->
            unzipStream(ZipInputStream(stream), output_path, filter)
        }
    }

    fun unzipFromBytes(
        zip_data: ByteArray,
        output_path: String,
        filter: (String) -> Boolean = { true }
    ) {
        ByteArrayInputStream(zip_data).use { stream ->
            unzipStream(ZipInputStream(stream), output_path, filter)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun unzipStream(
        zip_in: ZipInputStream,
        output_path: String,
        filter: (String) -> Boolean
    ) {
        zip_in.use {
            while (true) {
                val entry = zip_in.nextEntry ?: break

                val file_name = entry.name
                if (filter(file_name)) {
                    extractEntry(zip_in, entry, output_path, file_name)
                }
                zip_in.closeEntry()
            }
        }
    }

    private fun extractEntry(
        zip_in: ZipInputStream,
        entry: ZipEntry,
        output_path: String,
        file_name: String
    ) {
        val new_file = File(output_path, file_name)
        if (entry.isDirectory) {
            new_file.mkdirs()
        } else {
            new_file.parentFile?.mkdirs()
            FileOutputStream(new_file).use { output ->
                zip_in.copyTo(output)
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun basePath(file: File, flatten: Boolean): String {
        return if (flatten && file.isDirectory) "" else file.name
    }
}
