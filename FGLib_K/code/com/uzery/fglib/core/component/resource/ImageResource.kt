package com.uzery.fglib.core.component.resource

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.utils.data.image.ImageData

/**
 * [ImageResource] is one of [OnLoadComponent]
 *
 * Loads image from file
 *
 * @see [ObjectComponent]
 **/
class ImageResource(val name: String, vararg val effects: String): OnLoadComponent {
    override fun run() {
        ImageData.set(name, *effects)
    }
}
