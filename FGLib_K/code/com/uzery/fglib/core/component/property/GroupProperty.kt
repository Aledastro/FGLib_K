package com.uzery.fglib.core.component.property

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.ObjectComponent

/**
 * TODO("doc")
 **/
abstract class GroupProperty(vararg component: ObjectComponent): GroupComponent(*component)
