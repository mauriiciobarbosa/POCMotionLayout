package com.mauricio.poc.position.data

import com.mauricio.poc.position.components.PieChartView

internal sealed class PatrimonyState

internal data class PatrimonyViewData(
    val amountPatrimony: String,
    val amountInvestments: String,
    val amountAccount: String,
    val investmentTypes: List<PieChartView.Value>
) : PatrimonyState()