package com.uzery.fglib.core.component.resource

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.struct.num.IntI

/**
 * [SpriteResource] is one of [OnLoadComponent]
 *
 * Loads sprite from file
 *
 * @see [ObjectComponent]
 **/
class SpriteResource(val name: String, val size: IntI, vararg val effects: String): OnLoadComponent {
    override fun run() {
        ImageData.set(name, size, *effects)
    }
}
