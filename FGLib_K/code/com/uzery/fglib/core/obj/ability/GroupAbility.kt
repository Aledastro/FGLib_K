package com.uzery.fglib.core.obj.ability

import com.uzery.fglib.core.obj.GroupComponent
import com.uzery.fglib.core.obj.ObjectComponent

abstract class GroupAbility(vararg component: ObjectComponent): GroupComponent(*component)
