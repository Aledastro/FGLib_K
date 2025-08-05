package com.uzery.fglib.utils.history

interface AbstractHistoryAction<Action> {
    fun isRedundant(): Boolean

    operator fun unaryMinus(): Action
}
