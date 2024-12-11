package com.uzery.fglib.core.component.visual

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.ObjectComponent

/**
 * TODO("doc")
 **/
abstract class GroupVisualiser(vararg component: ObjectComponent): GroupComponent(*component)
