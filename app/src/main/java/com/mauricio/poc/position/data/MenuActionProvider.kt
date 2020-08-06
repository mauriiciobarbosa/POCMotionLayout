package com.mauricio.poc.position.data

import android.content.Context
import android.widget.Toast
import com.mauricio.poc.position.R

class MenuActionProvider(
    val context: Context
) {

    fun getActions(): List<MenuAction> {
        return listOf(
            createEasyAccountAction(),
            createInviteFriendsAction(),
            createLoanAction()
        )
    }

    private fun createEasyAccountAction(): MenuAction {
        return MenuAction(
            icon = R.drawable.ic_coin,
            title = context.getString(R.string.home_menu_easy_account),
            description = context.getString(R.string.home_menu_easy_account_description),
            action = {
                Toast.makeText(context, "Open easy account", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun createInviteFriendsAction(): MenuAction {
        return MenuAction(
            icon = R.drawable.ic_gift,
            title = context.getString(R.string.home_menu_invite_friends),
            description = context.getString(R.string.home_menu_invite_friends_description),
            action = {
                Toast.makeText(context, "Open invite friends", Toast.LENGTH_SHORT).show()
            },
            isNew = true
        )
    }

    private fun createLoanAction(): MenuAction {
        return MenuAction(
            icon = R.drawable.ic_loan_action,
            title = context.getString(R.string.home_menu_loan),
            description = context.getString(R.string.home_menu_loan_description),
            action = {
                Toast.makeText(context, "Open loan", Toast.LENGTH_SHORT).show()
            },
            isNew = true
        )
    }
}