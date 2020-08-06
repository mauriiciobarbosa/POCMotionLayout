package com.mauricio.poc.position.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mauricio.poc.position.data.MenuAction
import com.mauricio.poc.position.data.MenuActionProvider
import com.mauricio.poc.position.data.MenuActionSize

class HomeWithInvestmentsViewModel(
    private val menuActionProvider: MenuActionProvider
) {

    private val _actions = MutableLiveData<List<MenuAction>>()
    val actions: LiveData<List<MenuAction>> = _actions

    init {
        fillActions()
    }

    private fun fillActions() {
        val allActions = menuActionProvider
            .getActions()
            .filter(MenuAction::isEnabled)

        _actions.value = changeSizeIfNeeded(allActions)
    }

    private fun changeSizeIfNeeded(allActions: List<MenuAction>): List<MenuAction> {
        val hasSpaceOnLastItem = allActions.size % 2 != 0

        return allActions
            .takeIf { hasSpaceOnLastItem }
            ?.let {
                val lastAction = allActions.last().copy(size = MenuActionSize.BIG)
                allActions.toMutableList().apply {
                    removeAt(lastIndex)
                    add(lastAction)
                }
            } ?: allActions
    }
}