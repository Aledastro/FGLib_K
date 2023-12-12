package com.uzery.fglib.core.obj.controller

import com.uzery.fglib.core.obj.ObjectComponent

fun interface Controller: ObjectComponent {
    fun get(): () -> TempAction
}
