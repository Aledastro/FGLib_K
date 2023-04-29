package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
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
            origins[filename] = Image(getPath(filename))
        }

        fun get(filename: String, pos: IntI): Image {
            return sprites[filename]?.get(pos) ?: throw DebugData.error("no sprite from: $filename $pos")
        }

        fun set(filename: String, size: IntI, scale: Int = -1) {
            val path = getPath(filename)
            if(sprites[filename] != null) {
                if(sprites[filename]!!.size != size)
                    throw DebugData.error("duplicate sprite set: $filename old: [${sprites[filename]!!.size}] | new: [$size]")
                return
            }
            sprites[filename] = SpriteImage(path, size, scale)
        }

        private fun getPath(filename: String): String {
            return "C:\\Users\\User\\IdeaProjects\\lib\\FGLib_K\\project\\media\\images\\$filename"
        }
    }
}
