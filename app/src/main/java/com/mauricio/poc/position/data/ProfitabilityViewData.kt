package com.mauricio.poc.position.data

internal enum class ProfitabilityErrorType {
    MAINTENANCE, GENERIC, GENERAL
}

internal sealed class ProfitabilityState

internal data class SummaryProfitabilityViewData(
    val date: String,
    val period: String,
    val value: String,
    val percentage: String
) : ProfitabilityState()

internal data class ProfitabilityError(
    val type: ProfitabilityErrorType,
    val title: String,
    val description: String
) : ProfitabilityState() {
    fun isInMaintenance(): Boolean = type == ProfitabilityErrorType.MAINTENANCE

    fun isGeneral(): Boolean = type == ProfitabilityErrorType.GENERAL
}

internal object ProfitabilityLoading : ProfitabilityState()

internal data class ProfitabilityNoContent(val message: String) : ProfitabilityState()