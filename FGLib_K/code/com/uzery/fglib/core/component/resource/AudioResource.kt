package com.uzery.fglib.core.component.resource

import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.utils.audio.AudioData

/**
 * TODO("doc")
 **/
class AudioResource(val name: String): OnLoadComponent {
    override fun run() {
        AudioData.set(name)
    }
}
