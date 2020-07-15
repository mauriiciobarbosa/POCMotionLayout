package com.mauricio.poc.position.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

val Double?.moneyFormat: String
    get() {
        if (this == null) {
            return "R$ 0,00"
        }

        val numberFormat = NumberFormat.getNumberInstance(Locale("pt", "BR"))
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2

        return if (this >= 0) {
            "R$ " + numberFormat.format(this)
        } else {
            "- R$ " + numberFormat.format(-this)
        }
    }

fun Double?.toPercentFormat(
    isAbsoluteValue: Boolean = true,
    signal: String = "-",
    removeZeros: Boolean = false
): String {
    if (this == null) {
        return ""
    }

    val numberFormat = if (removeZeros) {
        DecimalFormat("#.##").apply {
            decimalFormatSymbols = DecimalFormatSymbols(Locale("pt", "BR"))
            roundingMode = RoundingMode.HALF_UP
        }
    } else {
        NumberFormat.getNumberInstance(Locale("pt", "BR")).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
            roundingMode = RoundingMode.HALF_UP
        }
    }

    val value = numberFormat.format(Math.abs(this)) + "%"

    return if (isAbsoluteValue || this >= 0) value else signal + value
}