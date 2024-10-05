package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.GroupComponent
import com.uzery.fglib.core.obj.ObjectComponent

abstract class GroupVisualiser(vararg component: ObjectComponent): GroupComponent(*component)
