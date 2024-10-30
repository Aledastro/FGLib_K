package com.uzery.fglib.core.component.listener

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.FGUtils
import java.util.*

class InputAction(val code: String, val prime: GameObject, vararg val args: Any) {
    val stats = prime.stats

    override fun toString() = "InputAction[$code]: ${args.contentToString()}"
}
