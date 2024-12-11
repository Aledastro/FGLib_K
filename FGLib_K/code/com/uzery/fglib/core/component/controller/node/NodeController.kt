package com.uzery.fglib.core.component.controller.node

import com.uzery.fglib.core.component.controller.GroupController

/**
 * TODO("doc")
 **/
class NodeController(
    default: TempActionNode,
    private vararg val nodes: TempActionNode
): GroupController() {
    init {
        addController {
            val res = nodes.firstOrNull { node -> node.choose_ready } ?: default
            res.action
        }

        nodes.forEach { addComponent(*it.components.toTypedArray()) }

        addAbility {
            nodes.forEach { it.update() }
        }
    }
}
