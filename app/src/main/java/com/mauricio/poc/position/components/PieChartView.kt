package com.mauricio.poc.position.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.mauricio.poc.position.R
import com.mauricio.poc.position.extensions.toPercentFormat

internal class PieChartView @JvmOverloads constructor(
    context: Context,
    private val values: List<Value>,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : PieChart(context, attrs, defStyle), OnChartValueSelectedListener {

    private var onValueSelected: ((Value?) -> Unit)? = null
    private var selectedValue: Value? = null
    private var shouldShowCenterText = false

    fun build(onValueSelected: ((Value?) -> Unit)? = null): PieChartView {
        this.onValueSelected = onValueSelected
        configureChart()
        setValuesIntoChart(createEntries())
        setOnChartValueSelectedListener(this)
        showAnimation()
        return this
    }

    private fun configureChart() {
        description.isEnabled = false
        isDrawHoleEnabled = true
        isRotationEnabled = false
        legend.isEnabled = false
        holeRadius = HOLE_RADIUS
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

    private fun configureCenterText() {
        setCenterTextSize(SIZE_FONT_CENTER)
        setCenterTextTypeface(ResourcesCompat.getFont(context, R.font.rational_display_medium))
        updateCenterText()
    }

    private fun showAnimation() {
        animateY(ANIMATION_DURATION, Easing.EaseInOutQuad)
    }

    override fun onNothingSelected() {
        highlightValues(null)
        onValueSelected?.invoke(null)
        selectedValue = null
        updateCenterText()
    }

    private fun updateCenterText() {
        if (shouldShowCenterText) {
            val value = selectedValue?.percentage ?: GRAPH_FULL_PERCENTAGE
            centerText = value.toDouble().toPercentFormat(removeZeros = true)
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val pieChartValue = e?.data as? Value
        onValueSelected?.invoke(pieChartValue)
        selectedValue = pieChartValue
        updateCenterText()
    }

    fun showCenterText() {
        shouldShowCenterText = true
        configureCenterText()
        updateCenterText()
    }

    fun hideCenterText() {
        shouldShowCenterText = false
        centerText = ""
    }

    data class Value(
        val color: String,
        val description: String,
        val percentage: Float,
        val value: Double
    )

    companion object {
        private const val GRAPH_FULL_PERCENTAGE = 100.0f
        private const val SIZE_FONT_CENTER = 18f
        private const val ANIMATION_DURATION = 1400
        private const val DISTANCE_FROM_CENTER = 5F
        private const val HOLE_RADIUS = 60F
        private const val DEFAULT_GRAPH_COLOR = "#FFD8D8D8"
    }
}