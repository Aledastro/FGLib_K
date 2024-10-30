package com.uzery.fglib.core.component.controller.node

import com.uzery.fglib.core.component.controller.GroupController

class StateController(
    default: TempActionNode,
    private val all_nodes: Array<TempActionNode>,
    private val now: () -> Array<TempActionNode>
): GroupController() {
    init {
        addController {
            val res = now().firstOrNull { node -> node.choose_ready } ?: default
            res.action
        }

        all_nodes.forEach { addComponent(*it.components.toTypedArray()) }

        addAbility {
            all_nodes.forEach { it.update() }
        }
    }
}
