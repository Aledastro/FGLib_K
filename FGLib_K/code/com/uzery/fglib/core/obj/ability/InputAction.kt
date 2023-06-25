package com.uzery.fglib.core.obj.ability

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.FGUtils
import java.util.*

data class InputAction(val code: CODE, private val full_info: String, val prime: GameObject) {

    enum class CODE {
        OPEN, INTERACT, INTERACT_I, INTERRUPT, INTERRUPT_I, IMPACT, PUSH, PUSH_I, TOUCH, TOUCH_I, DAMAGE
    }

    val stats = prime.stats
    val options = LinkedList<String>()
    val info: String

    init {
        info = resolve(full_info)
    }

    constructor(code: CODE, prime: GameObject): this(code, "no_info", prime)

    private fun resolve(info: String): String {
        if(!info.contains(" | ")) return info
        val tokenizer = StringTokenizer(FGUtils.subAfter(info, "|"))
        while(tokenizer.hasMoreTokens()) {
            options.add(tokenizer.nextToken())
        }
        return FGUtils.subBefore(info, "|", -1)
    }

    override fun toString() = "InputAction: $code - $info - ${prime.name}"
}