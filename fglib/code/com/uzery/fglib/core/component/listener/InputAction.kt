package com.uzery.fglib.core.component.listener

import com.uzery.fglib.core.obj.GameObject

/**
 * Primary class for exchanging info between [GameObject]
 *
 * @property code info channel
 * @property prime activator object
 * @property args info details
 **/
class InputAction(val code: String, val prime: GameObject, vararg val args: Any) {
    val stats = prime.stats

    override fun toString() = "InputAction[$code]: ${args.contentToString()}"
}
