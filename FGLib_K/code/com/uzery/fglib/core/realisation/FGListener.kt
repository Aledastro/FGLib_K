package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.input.KeyActivator
import com.uzery.fglib.utils.input.MouseActivator
import com.uzery.fglib.utils.input.data.FGKey

abstract class FGListener {
    abstract val keyboard: KeyActivator<FGKey>
    abstract val char_keyboard: KeyActivator<Char>
    abstract val mouse: MouseActivator
}
