package com.uzery.fglib.core.component.load

import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.utils.audio.AudioData
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
class AudioResource(val name: String): OnLoadComponent {
    override fun run() {
        AudioData.set(name)
    }
}
