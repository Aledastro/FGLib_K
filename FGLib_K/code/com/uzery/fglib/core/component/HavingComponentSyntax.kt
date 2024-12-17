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
interface HavingComponentSyntax {
    fun addComponent(vararg component: ObjectComponent)
}
