package com.uzery.fglib.core.component.resource

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.utils.audio.AudioData

/**
 * [AudioResource] is one of [OnLoadComponent]
 *
 * Loads audio from file
 *
 * @see [ObjectComponent]
 **/
class AudioResource(val name: String): OnLoadComponent {
    override fun run() {
        AudioData.set(name)
    }
}
