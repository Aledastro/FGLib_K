package com.uzery.fglib.core.component.listener

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.FGUtils
import java.util.*

data class InputAction(val code: String, val prime: GameObject, private val full_info: String = "no_info") {

    val stats = prime.stats
    val options = ArrayList<String>()
    lateinit var info: String

    init {
        resolve(full_info)
    }

    private fun resolve(info: String) {
        if (" | " !in info) {
            this.info = info
            return
        }
        val tokenizer = StringTokenizer(FGUtils.subAfter(info, "|"))
        while (tokenizer.hasMoreTokens()) {
            options.add(tokenizer.nextToken())
        }
        this.info = FGUtils.subBefore(info, "|", -1)
    }

    override fun toString() = "InputAction: $code - $info - ${prime.name}"
}