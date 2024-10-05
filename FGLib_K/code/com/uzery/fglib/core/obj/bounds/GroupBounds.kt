package com.uzery.fglib.core.obj.bounds

import com.uzery.fglib.core.obj.GroupComponent
import com.uzery.fglib.core.obj.ObjectComponent

abstract class GroupBounds(vararg component: ObjectComponent): GroupComponent(*component)
