package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntInt
import javafx.scene.image.Image

class Data {
    companion object {
        private val origins = HashMap<String, Image>()
        private val sprites = HashMap<String, SpriteImage>()
        fun get(filename: String): Image {
            val input = origins[filename]
            return if(input == null) {
                println("deprecated: $filename")
                val img = Image(filename)
                origins[filename] = img
                img
            } else input
        }

        fun set(filename: String) {
            if(origins[filename] != null) return
            origins[filename] = Image(filename)
        }

        fun get(filename: String, pos: IntInt): Image {
            return sprites[filename]?.get(pos) ?: throw DebugData.error("no sprite from: $filename $pos")
        }

        fun set(filename: String, size: IntInt) {
            if(sprites[filename] != null) {
                if(sprites[filename]!!.size != size)
                    throw DebugData.error("duplicate sprite set: $filename old: [${sprites[filename]!!.size}] | new: [$size]")
                return
            }
            sprites[filename] = SpriteImage(filename, size)
        }
    }
}
