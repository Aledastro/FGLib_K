package com.uzery.fglib.core.obj.listener

import com.uzery.fglib.core.obj.GroupComponent
import com.uzery.fglib.core.obj.ObjectComponent

abstract class GroupListener(vararg component: ObjectComponent): GroupComponent(*component)
