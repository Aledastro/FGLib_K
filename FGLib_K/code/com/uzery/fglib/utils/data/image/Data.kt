package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.CollectDataClass
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

object Data: CollectDataClass() {
    private val origins = HashMap<String, Image>()
    private val sprites = HashMap<String, SpriteImage>()
    private val combinations = HashMap<String, CombinationImage>()

    fun sprite_set(name: String): IntI {
        val img = sprites[name] ?: throw DebugData.error("no sprite from: $name")
        return img.size
    }

    fun sprite_get(name: String): IntI {
        val img = sprites[name] ?: throw DebugData.error("no sprite from: $name")
        return img.origin_size/img.size
    }

    fun get(name: String) = origins[name] ?: throw DebugData.error("no origin from: $name")

    fun set(name: String) {
        if (origins[name] != null) return

        try {
            origins[name] = Image(resolvePath(name))
        } catch (e: Exception) {
            throw DebugData.error("Data set: in $name: ${resolvePath(name)}")
        }
    }

    fun get(name: String, pos: IntI) =
        sprites[name]?.get(pos) ?: throw DebugData.error("no sprite from: $name $pos")

    fun set(name: String, size: IntI, scale: Int = -1) {
        if (sprites[name] != null) {
            if (sprites[name]!!.size != size)
                throw DebugData.error("duplicate sprite set: $name old: [${sprites[name]!!.size}] | new: [$size]")
            return
        }
        sprites[name] = SpriteImage(resolvePath(name), size, scale)
    }

    fun setCombination(name: String, size: IntI, rule: ImageCombinationRule, scale: Int = -1) {
        if (combinations[name] != null) {
            if (combinations[name]!!.size != size)
                throw DebugData.error("duplicate combination set: $name old: [${combinations[name]!!.size}] | new: [$size]")
            return
        }
        combinations[name] = CombinationImage(resolvePath(name), size, rule, scale)
    }

    fun combination(name: String, pos: IntI): Image {
        return combinations[name]?.get(pos) ?: throw DebugData.error("no combination from: $name $pos")
    }
}
