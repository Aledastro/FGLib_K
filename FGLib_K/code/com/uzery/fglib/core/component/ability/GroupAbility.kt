package com.uzery.fglib.core.component.ability

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.ObjectComponent

abstract class GroupAbility(vararg component: ObjectComponent): GroupComponent(*component)
