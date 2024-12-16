package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.HavingComponentSyntax
import com.uzery.fglib.core.component.ObjectComponent

/**
 * [GroupController] is just [GroupComponent] under the hood
 *
 * Can use `addController()` via [HavingComponentSyntax]
 *
 * @see ObjectComponent
 **/
abstract class GroupController(vararg component: ObjectComponent): GroupComponent(*component)
