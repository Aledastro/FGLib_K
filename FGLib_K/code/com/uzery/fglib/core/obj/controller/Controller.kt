package com.uzery.fglib.core.obj.controller

fun interface Controller {
    fun get(): () -> TempAction
}
