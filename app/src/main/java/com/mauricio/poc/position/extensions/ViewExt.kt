package com.mauricio.poc.position.extensions

import android.view.View

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun String.hideMoney(characters: String = "***"): String {
    return this.replaceAfter('$', " $characters")
}