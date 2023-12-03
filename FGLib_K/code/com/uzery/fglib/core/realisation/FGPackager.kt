package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.data.audio.FGMedia
import com.uzery.fglib.utils.data.audio.FGMediaPlayer
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

abstract class FGPackager {
    abstract fun fromColor(c: String): FGColor
    abstract fun fromMouseKey(key: String): FGMouseKey
    abstract fun fromKey(key: String): FGKey

    abstract fun fromFGColor(c: FGColor): Any
    abstract fun fromFGFont(f: FGFont): Any
    abstract fun fromFGKey(key: FGKey): Any
    abstract fun fromFGMouseKey(key: FGMouseKey): Any
    abstract fun fromFGMedia(media: FGMedia): Any

    abstract fun fromFGMediaPlayer(media: FGMediaPlayer): Any
}
