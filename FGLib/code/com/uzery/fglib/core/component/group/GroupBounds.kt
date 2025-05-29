package com.uzery.fglib.core.component.group

import com.uzery.fglib.core.component.ComponentFunctionality
import com.uzery.fglib.core.component.ObjectComponent

/**
 * Just [GroupComponent] under the hood
 *
 * Can use `addBounds()` via [ComponentFunctionality]
 *
 * @see [ObjectComponent]
 **/
abstract class GroupBounds(vararg component: ObjectComponent): GroupComponent(*component)
