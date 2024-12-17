package com.uzery.fglib.core.component.group

import com.uzery.fglib.core.component.HavingComponentSyntax
import com.uzery.fglib.core.component.ObjectComponent

/**
 * Just [GroupComponent] under the hood
 *
 * Can use `addAbility()` via [HavingComponentSyntax]
 *
 * @see [ObjectComponent]
 **/
abstract class GroupAbility(vararg component: ObjectComponent): GroupComponent(*component)
