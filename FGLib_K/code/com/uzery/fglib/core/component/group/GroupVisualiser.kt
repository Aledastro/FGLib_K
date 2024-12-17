package com.uzery.fglib.core.component.group

import com.uzery.fglib.core.component.HavingComponentSyntax
import com.uzery.fglib.core.component.ObjectComponent

/**
 * Just [GroupComponent] under the hood
 *
 * Can use `addVisual()` via [HavingComponentSyntax]
 *
 * @see [ObjectComponent]
 **/
abstract class GroupVisualiser(vararg component: ObjectComponent): GroupComponent(*component)
