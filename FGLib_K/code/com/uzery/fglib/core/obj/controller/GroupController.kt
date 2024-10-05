package com.uzery.fglib.core.obj.controller

import com.uzery.fglib.core.obj.GroupComponent
import com.uzery.fglib.core.obj.ObjectComponent

abstract class GroupController(vararg component: ObjectComponent): GroupComponent(*component)
