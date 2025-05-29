package com.uzery.fglib.core.component.group

import com.uzery.fglib.core.component.ComponentFunctionality
import com.uzery.fglib.core.component.ObjectComponent

/**
 * Just [GroupComponent] under the hood
 *
 * Can use `addListener()` via [ComponentFunctionality]
 *
 * @see [ObjectComponent]
 **/
abstract class GroupListener(vararg component: ObjectComponent): GroupComponent(*component)
