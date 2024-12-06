package com.uzery.fglib.core.component.load

import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.utils.data.image.ImageData

class ImageResource(val name: String, vararg val effects: String): OnLoadComponent {
    override fun run() {
        ImageData.set(name, *effects)
    }
}
