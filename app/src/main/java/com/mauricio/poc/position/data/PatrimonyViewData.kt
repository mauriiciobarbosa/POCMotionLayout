package com.mauricio.poc.position.data

import com.mauricio.poc.position.components.PieChartView

internal enum class PatrimonyErrorType {
    GENERIC, GENERAL
}

internal sealed class PatrimonyState {
    fun toPatrimonyViewData(): PatrimonyViewData {
        val value = this
        check(value is PatrimonyViewData) { "${value.javaClass} is not a Success type" }
        return value
    }
}

internal data class PatrimonyViewData(
    val amountPatrimony: String,
    val amountInvestments: String,
    val amountAccount: String,
    val investmentTypes: List<PieChartView.Value>
) : PatrimonyState()

internal data class PatrimonyError(
    val type: PatrimonyErrorType,
    val title: String,
    val description: String
) : PatrimonyState() {
    fun isGeneral(): Boolean = type == PatrimonyErrorType.GENERAL
}

internal object PatrimonyLoading : PatrimonyState()