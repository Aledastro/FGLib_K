package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.CollectDataClass
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image

object ImageData: CollectDataClass() {
    private val origins = HashMap<String, Image>()
    private val sprites = HashMap<String, SpriteImage>()
    private val combinations = HashMap<String, CombinationImage>()

    private fun decode(name: String, vararg effects: String): String {
        var entry = name
        effects.forEach { entry += "#$it" }
        return entry
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun set(name: String, vararg effects: String) {
        val decode = decode(name, *effects)
        if (origins[decode] != null) return

        try {
            origins[decode] = ImageUtils.from(name, *effects)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DebugData.error("Data set: in $name: ${resolvePath(name)} ($decode) with error: $e")
        }
    }

    fun set(name: String, effects: List<String>) {
        set(name, *effects.toTypedArray())
    }

    fun get(name: String, vararg effects: String): Image {
        return origins[decode(name, *effects)] ?: throw DebugData.error("no origin from: $name")
    }

    fun get(name: String, effects: List<String>): Image {
        return get(name, *effects.toTypedArray())
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun sprite_set(name: String, vararg effects: String): IntI {
        val img = sprites[decode(name, *effects)]
            ?: throw DebugData.error("no sprite from: ${decode(name, *effects)}")
        return img.size
    }

    fun sprite_get(name: String, vararg effects: String): IntI {
        val img = sprites[decode(name, *effects)]
            ?: throw DebugData.error("no sprite from: ${decode(name, *effects)}")
        return img.origin_size/img.size
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun set(name: String, size: IntI, vararg effects: String) {
        val decode = decode(name, *effects)
        if (sprites[decode] != null) {
            if (sprites[decode]!!.size != size)
                throw DebugData.error("duplicate sprite set: $decode old: [${sprites[decode]!!.size}] | new: [$size]")
            return
        }
        try {
            sprites[decode] = SpriteImage(ImageUtils.from(name), "${resolvePath(name)} ($decode)", size, *effects)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DebugData.error("from: ${resolvePath(name)} ($decode) with error: $e")
        }
    }

    fun set(name: String, size: IntI, effects: List<String>) {
        set(name, size, *effects.toTypedArray())
    }

    fun get(name: String, pos: IntI, vararg effects: String): Image {
        return sprites[decode(name, *effects)]?.get(pos) ?: throw DebugData.error("no sprite from: $name $pos")
    }

    fun get(name: String, pos: IntI, effects: List<String>): Image {
        return get(name, pos, *effects.toTypedArray())
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun setCombination(name: String, size: IntI, rule: ImageCombinationRule) {
        if (combinations[name] != null) {
            if (combinations[name]!!.size != size)
                throw DebugData.error("duplicate combination set: $name old: [${combinations[name]!!.size}] | new: [$size]")
            return
        }
        //todo
        combinations[name] = CombinationImage(resolvePath(name), size, rule)
    }

    fun combination(name: String, pos: IntI): Image {
        //todo
        return combinations[name]?.get(pos) ?: throw DebugData.error("no combination from: $name $pos")
    }
}
