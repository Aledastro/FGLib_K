package com.uzery.fglib.utils.history


abstract class AbstractHistoryManager<Action: AbstractHistoryAction<Action>> {
    val history = ArrayList<Action>()
    val new_history = ArrayList<Action>()

    protected fun saveHistory(action: Action) {
        if (action.isRedundant()) return

        history.add(action)
    }

    abstract fun applyAction(action: Action)

    fun undo() {
        if (history.isEmpty()) return

        val action = history.removeLast()
        applyAction(-action)
        new_history.add(action)
    }

    fun redo() {
        if (new_history.isEmpty()) return

        val action = new_history.removeLast()
        applyAction(action)
        history.add(action)
    }
}
