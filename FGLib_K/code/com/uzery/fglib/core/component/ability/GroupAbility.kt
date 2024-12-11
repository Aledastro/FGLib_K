package com.uzery.fglib.core.component.ability

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.HavingComponentSyntax

/**
 * [GroupAbility] is just [GroupComponent] under the hood
 *
 * Only difference is that it can use addAbility() function using [HavingComponentSyntax]
 *
 * @see ObjectComponent
 **/
abstract class GroupAbility(vararg component: ObjectComponent): GroupComponent(*component)
