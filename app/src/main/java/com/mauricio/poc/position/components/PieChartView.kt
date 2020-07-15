package com.mauricio.poc.position.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

internal class PieChartView @JvmOverloads constructor(
    context: Context,
    private val values: List<Value>,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : PieChart(context, attrs, defStyle) {

    fun build(): PieChartView {
        configureChart()
        setValuesIntoChart(createEntries())
        showAnimation()
        return this
    }

    private fun configureChart() {
        description.isEnabled = false
        isDrawHoleEnabled = true
        isRotationEnabled = false
        legend.isEnabled = false
        holeRadius =
            HOLE_RADIUS
    }

    private fun createEntries(): Map<PieEntry, Int> {
        val currentValues = if (values.isEmpty()) generateEmptyValue() else values

        return currentValues.map { value ->
            val color = pickColor(from = value.color)
            PieEntry(value.percentage, value) to color
        }.toMap()
    }

    private fun generateEmptyValue() = listOf(
        Value(
            color = DEFAULT_GRAPH_COLOR,
            description = "",
            percentage = 0F,
            value = 0.0
        )
    )

    private fun pickColor(from: String): Int {
        val source = if (from.isEmpty()) DEFAULT_GRAPH_COLOR else from
        return Color.parseColor(source)
    }

    private fun setValuesIntoChart(entries: Map<PieEntry, Int>) {
        val pieDataSet = PieDataSet(entries.keys.toList(), "").apply {
            setDrawValues(false)
            selectionShift =
                DISTANCE_FROM_CENTER
            colors = entries.values.toList()
        }
        data = PieData(pieDataSet)
    }

    private fun showAnimation() {
        animateY(ANIMATION_DURATION, Easing.EaseInOutQuad)
    }

    fun onChatSelected() {
        highlightValues(null)
    }

    data class Value(
        val color: String,
        val description: String,
        val percentage: Float,
        val value: Double
    )

    companion object {
        private const val ANIMATION_DURATION = 1400
        private const val DISTANCE_FROM_CENTER = 5F
        private const val HOLE_RADIUS = 60F
        private const val DEFAULT_GRAPH_COLOR = "#FFD8D8D8"
    }
}