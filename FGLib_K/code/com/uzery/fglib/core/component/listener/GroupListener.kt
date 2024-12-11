package com.uzery.fglib.core.component.listener

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.ObjectComponent

/**
 * TODO("doc")
 **/
abstract class GroupListener(vararg component: ObjectComponent): GroupComponent(*component)
