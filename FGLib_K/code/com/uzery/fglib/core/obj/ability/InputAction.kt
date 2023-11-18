package com.uzery.fglib.core.obj.ability

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.FGUtils
import java.util.*

data class InputAction(val code: String, val prime: GameObject, private val full_info: String = "no_info") {

    val stats = prime.stats
    val options = ArrayList<String>()
    val info: String

    init {
        info = resolve(full_info)
    }

    private fun resolve(info: String): String {
        if (" | " !in info) return info
        val tokenizer = StringTokenizer(FGUtils.subAfter(info, "|"))
        while (tokenizer.hasMoreTokens()) {
            options.add(tokenizer.nextToken())
        }
        return FGUtils.subBefore(info, "|", -1)
    }

    override fun toString() = "InputAction: $code - $info - ${prime.name}"
}