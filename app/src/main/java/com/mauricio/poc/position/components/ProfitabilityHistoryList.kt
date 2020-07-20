package com.mauricio.poc.position.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mauricio.poc.position.R
import com.mauricio.poc.position.data.SummaryProfitabilityViewData
import com.mauricio.poc.position.extensions.animateTextChange
import com.mauricio.poc.position.extensions.hideMoney
import com.mauricio.poc.position.extensions.isVisible
import com.mauricio.poc.position.extensions.setMoneyColor
import kotlinx.android.synthetic.main.layout_profitability_history_list_item.view.*

internal class ProfitabilityHistoryList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var isAbleToShowMoney: Boolean = true
    private val currentItems: MutableList<View> = mutableListOf()
    private val currentValues: MutableList<SummaryProfitabilityViewData> = mutableListOf()

    init {
        orientation = VERTICAL

        if (isInEditMode) {
            addPreviewData()
        }
    }

    private fun addPreviewData() {
        val list = listOf(
            SummaryProfitabilityViewData(
                date = "Fev 2020", period = "Rendimento de 02 a 28/02",
                value = "+ R$ 1.000.000,00", percentage = "+ 99,99%"
            ),
            SummaryProfitabilityViewData(
                date = "Jan 2020", period = "Rendimento de 01 a 31/01",
                value = "- R$ 1.000.000,00", percentage = "- 100%"
            )
        )
        setupHistory(list)
    }

    fun setupHistory(list: List<SummaryProfitabilityViewData>) {
        currentValues.clear()
        currentValues.addAll(list)

        currentItems.clear()
        currentValues.forEachIndexed { index, data ->
            currentItems += createRow(data, index == 0)
        }
    }

    private fun createRow(
        data: SummaryProfitabilityViewData,
        isFirstItem: Boolean,
        params: ViewGroup.LayoutParams? = null
    ): View {
        val item = createListItem(data, isFirstItem).apply { tag = data.date }

        if (params != null) {
            addView(item, params)
        } else {
            addView(item)
        }

        return item
    }

    private fun createListItem(data: SummaryProfitabilityViewData, isFirstItem: Boolean): View {
        return inflate(context, R.layout.layout_profitability_history_list_item, null).apply {
            textViewProfitabilityHistoryDate.text = data.date
            textViewProfitabilityHistoryValue.text = prepareMoneyValue(data.value)
            textViewProfitabilityHistoryValue.setMoneyColor(
                colorPositive = R.color.dark_rise,
                colorNegative = R.color.nightfall
            )
            textViewProfitabilityHistoryPercentage.text = data.percentage
            textViewProfitabilityHistoryPercentage.setMoneyColor(
                colorPositive = R.color.dark_rise,
                colorNegative = R.color.nightfall
            )
            topSeparator.isVisible(isFirstItem)
        }
    }

    private fun prepareMoneyValue(moneyValue: String): String {
        return if (isAbleToShowMoney) moneyValue else moneyValue.hideMoney()
    }

    fun showMoney() {
        currentItems.forEachIndexed { index, view ->
            val textView = view.findViewById<TextView>(R.id.textViewProfitabilityHistoryValue)
            val profitabilityValue = currentValues.getOrNull(index)?.value.orEmpty()
            textView.animateTextChange(profitabilityValue)
        }
        isAbleToShowMoney = true
    }

    fun hideMoney() {
        currentItems.forEachIndexed { index, view ->
            val textView = view.findViewById<TextView>(R.id.textViewProfitabilityHistoryValue)
            val profitabilityValue = currentValues.getOrNull(index)?.value.orEmpty().hideMoney()
            textView.animateTextChange(profitabilityValue)
        }
        isAbleToShowMoney = true
    }
}