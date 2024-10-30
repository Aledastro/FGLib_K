package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.ObjectComponent

abstract class GroupBounds(vararg component: ObjectComponent): GroupComponent(*component)
