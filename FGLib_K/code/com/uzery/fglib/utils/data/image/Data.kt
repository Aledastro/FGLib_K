package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image
import kotlin.math.max

class Data {
    companion object {
        private val origins = HashMap<String, Image>()
        private val sprites = HashMap<String, SpriteImage>()
        private val combinations = HashMap<String, CombinationImage>()
        private var filename = ""

        fun setInitDirectory(directory: String) {
            filename = directory
        }

        fun get(name: String) = origins[name] ?: throw DebugData.error("no origin from: $name")

        fun set(name: String) {
            if(origins[name] != null) return
            origins[name] = Image(resolvePath(name))
        }

        fun get(name: String, pos: IntI) =
            sprites[name]?.get(pos) ?: throw DebugData.error("no sprite from: $name $pos")

        fun set(name: String, size: IntI, scale: Int = -1) {
            if(sprites[name] != null) {
                if(sprites[name]!!.size != size)
                    throw DebugData.error("duplicate sprite set: $name old: [${sprites[name]!!.size}] | new: [$size]")
                return
            }
            sprites[name] = SpriteImage(resolvePath(name), size, scale)
        }

        fun setCombination(name: String, size: IntI, rule: ImageCombinationRule, scale: Int = -1) {
            if(combinations[name] != null) {
                if(combinations[name]!!.size != size)
                    throw DebugData.error("duplicate combination set: $name old: [${combinations[name]!!.size}] | new: [$size]")
                return
            }
            combinations[name] = CombinationImage(resolvePath(name), size, rule, scale)
        }

        fun combination(name: String, pos: IntI): Image {
            return combinations[name]?.get(pos) ?: throw DebugData.error("no combination from: $name $pos")
        }

        private fun resolvePath(name: String): String {
            //todo move from fglib to project files
            var local_path = ""
            var last = name
            if(name.indexOf('|') != -1) {
                local_path = when(name.substring(0, name.indexOf('|'))) {
                    "wld" -> "world/"
                    "map" -> "world/map/"
                    "mob" -> "world/enemy/"
                    "char" -> "world/character/"
                    "item" -> "world/items/"
                    "ui" -> "ui/"
                    else -> ""
                }
                last = name.substring(max(0, name.indexOf('|') + 1))
            }

            return "$filename$local_path$last".replace('/', '\\')
        }
    }
}
