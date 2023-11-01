package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.getter.FGFormat
import com.uzery.fglib.utils.data.image.effects.*
import javafx.scene.image.WritableImage

object ImageTransformUtils {
    private val map = HashMap<String, ImageTransformEffect>()

    init {
        addEffect(ReverseX, ReverseY, ReverseXY, TurnCW, TurnCCW, TurnCW90, TurnCCW90, UniColor)
    }

    fun addEffect(vararg effects: ImageTransformEffect) {
        effects.forEach { effect ->
            if(map.containsKey(effect.name)) throw DebugData.error("duplicate image effect: ${effect.name}")
            map[effect.name] = effect
        }
    }

    fun applyEffect(origin: WritableImage, effect: String): WritableImage {
        val effect_name = FGFormat[effect].first
        val effect_args = FGFormat[effect].second

        val transform = map[effect_name] ?: throw DebugData.error("unknown image effect: $effect")

        return transform[origin, effect_args]
    }
}
