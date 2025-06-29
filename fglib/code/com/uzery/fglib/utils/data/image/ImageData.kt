package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.CollectDataClass
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
object ImageData: CollectDataClass() {
    private val origins = HashMap<String, FGImage>()
    private val sprites = HashMap<String, SpriteImage>()

    private fun decode(name: String, vararg effects: String): String {
        return buildString {
            append(name)
            effects.forEach { append("#$it") }
        }
    }

    private fun keyFrom(name: String, vararg effects: String): String {
        return resolvePath(name)+" - "+decode(name, *effects)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun set(name: String, vararg effects: String) {
        val decode = decode(name, *effects)
        val key = keyFrom(name, *effects)

        if (origins[key] != null) return

        try {
            origins[key] = ImageUtils.from(name, *effects)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DebugData.error("Data set: ${resolvePath(name)} ($decode) with error: $e")
        }
    }

    fun set(name: String, effects: List<String>) {
        set(name, *effects.toTypedArray())
    }

    operator fun get(name: String, vararg effects: String): FGImage {
        val decode = decode(name, *effects)
        val key = keyFrom(name, *effects)

        return origins[key] ?: throw DebugData.error("no origin from: $decode (${resolvePath(name)})")
    }

    operator fun get(name: String, effects: List<String>): FGImage {
        return get(name, *effects.toTypedArray())
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun sprite_set(name: String, vararg effects: String): IntI {
        val decode = decode(name, *effects)
        val key = keyFrom(name, *effects)

        val img = sprites[key]
            ?: throw DebugData.error("no sprite from: $decode")
        return img.sprite_size
    }

    fun sprite_get(name: String, vararg effects: String): IntI {
        val decode = decode(name, *effects)
        val key = keyFrom(name, *effects)

        val img = sprites[key]
            ?: throw DebugData.error("no sprite from: $decode")
        return img.origin.size/img.sprite_size
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun set(name: String, size: IntI, vararg effects: String) {
        val decode = decode(name, *effects)
        val key = keyFrom(name, *effects)

        if (sprites[key] != null) {
            if (sprites[key]!!.sprite_size != size) {
                throw DebugData.error("duplicate sprite set: $decode old: [${sprites[key]!!.sprite_size}] | new: [$size]")
            }
            return
        }
        try {
            sprites[key] = SpriteImage(ImageUtils.from(name), "${resolvePath(name)} ($decode)", size, *effects)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DebugData.error("Data set: ${resolvePath(name)} ($decode) with size: $size and error: $e")
        }
    }

    fun set(name: String, size: IntI, effects: List<String>) {
        set(name, size, *effects.toTypedArray())
    }

    operator fun get(name: String, pos: IntI, vararg effects: String): FGImage {
        val decode = decode(name, *effects)
        val key = keyFrom(name, *effects)

        return sprites[key]?.get(pos) ?: throw DebugData.error("no sprite from: $decode (${resolvePath(name)}) $pos")
    }

    operator fun get(name: String, pos: IntI, effects: List<String>): FGImage {
        return get(name, pos, *effects.toTypedArray())
    }

    fun write(image: FGImage, path: String) {
        ImageUtils.write(image, resolvePath(path))
    }
}
