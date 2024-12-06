package com.uzery.fglib.core.component.load

import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.struct.num.IntI

class SpriteResource(val name: String, val size: IntI, vararg val effects: String): OnLoadComponent {
    override fun run() {
        ImageData.set(name, size, *effects)
    }
}
