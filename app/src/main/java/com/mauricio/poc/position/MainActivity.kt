package com.mauricio.poc.position

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.components.PieChartView
import com.mauricio.poc.position.data.PatrimonyViewData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPatrimonyView()
    }

    private fun setupPatrimonyView() {
        val patrimonyState = PatrimonyViewData(
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
        patrimonyView.setupState(patrimonyState)
    }
}