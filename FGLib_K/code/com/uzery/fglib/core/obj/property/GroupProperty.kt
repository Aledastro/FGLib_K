package com.uzery.fglib.core.obj.property

import com.uzery.fglib.core.obj.GroupComponent
import com.uzery.fglib.core.obj.ObjectComponent

abstract class GroupProperty(vararg component: ObjectComponent): GroupComponent(*component)
