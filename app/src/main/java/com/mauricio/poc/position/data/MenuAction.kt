package com.mauricio.poc.position.data

import androidx.annotation.DrawableRes

data class MenuAction(
    @DrawableRes val icon: Int,
    val title: String,
    val description: String,
    val action: () -> Unit,
    val size: MenuActionSize = MenuActionSize.SMALL,
    val isNew: Boolean = false,
    val isEnabled: Boolean = true // a ser substituido por remote config
)