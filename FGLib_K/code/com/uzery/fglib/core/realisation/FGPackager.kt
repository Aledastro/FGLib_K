package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

abstract class FGPackager<RealisationColor, RealisationFont, RealisationKey, RealisationMouseKey> {
    abstract fun fromColor(c: String): FGColor
    abstract fun fromMouseKey(key: String): FGMouseKey
    abstract fun fromKey(key: String): FGKey

    abstract fun fromFGColor(c: FGColor): RealisationColor
    abstract fun fromFGFont(f: FGFont): RealisationFont
    abstract fun fromFGKey(key: FGKey): RealisationKey
    abstract fun fromFGMouseKey(key: FGMouseKey): RealisationMouseKey
}