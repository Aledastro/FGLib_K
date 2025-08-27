package com.uzery.fglib.utils.file

object FilenameUtils {
    fun fullNameOf(filename: String): String {
        val res = filename.replace("\\", "/")

        val id = res.lastIndexOf('/')
        return if (id == -1) res else res.substring(id+1)
    }

    fun dirOf(filename: String): String {
        val res = filename.replace("\\", "/")

        val id = res.lastIndexOf('/')
        return if (id == -1) "" else res.substring(0, id+1)
    }

    fun nameOf(filename: String): String {
        val name = fullNameOf(filename)
        val id = name.lastIndexOf('.')
        return if (id == -1) name else name.substring(0, id)
    }

    fun extensionOf(filename: String): String? {
        val name = fullNameOf(filename)
        val id = name.lastIndexOf('.')
        return if (id == -1) null else name.substring(id+1)
    }
}
