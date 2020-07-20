package com.mauricio.poc.position.data

import com.mauricio.poc.position.components.PieChartView

internal object Repository {
    fun loadPatrimonyData(): PatrimonyViewData {
        return PatrimonyViewData(
            amountPatrimony = "R$ 12.359.049,93",
            amountAccount = "R$ 1.515.222,68",
            amountInvestments = "R$ 10.843.827,25",
            investmentTypes = listOf(
                PieChartView.Value(
                    color = "#4F034F",
                    value = 1962110.61,
                    percentage = 16.40f,
                    description = "Tesouro Direto e Títulos Públicos"
                ),
                PieChartView.Value(
                    color = "#87027B",
                    value = 6000000.0,
                    percentage = 50.2f,
                    description = "Renda Fixa Privada"
                ),
                PieChartView.Value(
                    color = "#3CCBDA",
                    value = 1000000.0,
                    percentage = 8.35f,
                    description = "Fundos de Investimento"
                ),
                PieChartView.Value(
                    color = "#E6258C",
                    value = 1000000.0,
                    percentage = 8.35f,
                    description = "COE"
                ),
                PieChartView.Value(
                    color = "#FFBA00",
                    value = 1000000.0,
                    percentage = 8.35f,
                    description = "Renda Variável"
                ),
                PieChartView.Value(
                    color = "#9AC000",
                    value = 1000000.0,
                    percentage = 8.35f,
                    description = "Previdência Privada"
                )
            )
        )
    }

    fun loadProfitabilityData(): ProfitabilityViewData {
        return ProfitabilityViewData(
            current = SummaryProfitabilityViewData(
                date = "Março 2020",
                period = "Rendimento de 03 a 19/03",
                value = "+ R$ 1.000.000,00",
                percentage = "+ 2,78%"
            ),
            history = listOf(
                SummaryProfitabilityViewData(
                    date = "Fev 2020", period = "Rendimento de 02 a 28/02",
                    value = "+ R$ 1M", percentage = "+ 99,99%"
                ),
                SummaryProfitabilityViewData(
                    date = "Jan 2020", period = "Rendimento de 01 a 31/01",
                    value = "- R$ 999.999,99", percentage = "- 99%"
                )
            )
        )
    }
}