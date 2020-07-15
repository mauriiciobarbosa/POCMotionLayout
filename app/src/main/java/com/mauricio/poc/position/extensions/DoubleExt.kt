package com.mauricio.poc.position.extensions

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