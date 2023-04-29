package com.uzery.fglib.core.obj.controller

interface Controller {
    fun get(): () -> TempAction
}
