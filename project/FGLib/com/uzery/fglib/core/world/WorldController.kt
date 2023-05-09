package com.uzery.fglib.core.world

interface WorldController {
    fun ready(): Boolean
    fun changeRoom()

    fun init()

    fun update()
}
