package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.ObjectComponent

abstract class GroupController(vararg component: ObjectComponent): GroupComponent(*component)
