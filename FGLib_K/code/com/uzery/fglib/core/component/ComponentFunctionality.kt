package com.uzery.fglib.core.component

/**
 * Special component syntax for creating generic [ObjectComponent]
 *
 * for example:
 * ```
 * addAbility {
 *     time++
 * }
 * ```
 **/
interface ComponentFunctionality {
    fun addComponent(vararg component: ObjectComponent)
}
