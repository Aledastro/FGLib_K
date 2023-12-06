package com.uzery.fglib.utils.audio

import com.uzery.fglib.utils.data.CollectDataClass

object AudioData: CollectDataClass() {
    private val origins = HashMap<String, FGAudio>()
    operator fun get(name: String): FGAudio {
        return origins[resolvePath(name)]!!
    }

    fun set(name: String) {
        val decode = resolvePath(name)

        if (origins[decode] != null) return

        origins[decode] = FGAudio(decode)
    }
}
