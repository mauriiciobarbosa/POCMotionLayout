package com.mauricio.poc.position.extensions

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.mauricio.poc.position.R

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

fun TextView.setMoneyColor(
    colorPositive: Int = R.color.dark_rise,
    colorNegative: Int = R.color.nightfall
) {
    when {
        text.contains("+") -> setTextColor(ContextCompat.getColor(context, colorPositive))
        text.contains("-") -> setTextColor(ContextCompat.getColor(context, colorNegative))
        else -> {
            this.setTextColor(Color.BLACK)
        }
    }
}

fun View.isVisible(show: Boolean) {
    visibility = if (show) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun Context.dpToPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)