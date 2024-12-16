package com.uzery.fglib.core.component.ability

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.HavingComponentSyntax
import com.uzery.fglib.core.component.ObjectComponent

/**
 * [GroupAbility] is just [GroupComponent] under the hood
 *
 * Can use `addAbility()` via [HavingComponentSyntax]
 *
 * @see ObjectComponent
 **/
abstract class GroupAbility(vararg component: ObjectComponent): GroupComponent(*component)
