package com.uzery.fglib.core.component.listener

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.HavingComponentSyntax
import com.uzery.fglib.core.component.ObjectComponent

/**
 * [GroupListener] is just [GroupComponent] under the hood
 *
 * Can use `addListener()` via [HavingComponentSyntax]
 *
 * @see ObjectComponent
 **/
abstract class GroupListener(vararg component: ObjectComponent): GroupComponent(*component)
