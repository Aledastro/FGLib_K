package com.uzery.fglib.core.program

import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.audio.AudioData
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.image.ImageData

abstract class LaunchConfiguration {
    abstract val main: Extension
    abstract val options: LaunchOptions

    fun init() {
        updatePathDirs()
        updatePaths()
        init0()
    }

    protected open fun updatePathDirs() {
        val dir = FGUtils.project_dir
        TextData.dir = "$dir/media/files/"
        ImageData.dir = "$dir/media/images/"
        AudioData.dir = "$dir/media/audio/"
    }

    protected open fun updatePaths() {}
    protected open fun init0() {}
}
