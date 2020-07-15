package com.mauricio.poc.position.components

import android.content.Context
import android.util.AttributeSet
import android.widget.AbsListView
import androidx.constraintlayout.motion.widget.MotionLayout
import com.mauricio.poc.position.data.PatrimonyViewData
import com.mauricio.poc.position.R
import com.mauricio.poc.position.extensions.animateTextChange
import com.mauricio.poc.position.extensions.hideMoney
import com.mauricio.poc.position.extensions.visible
import kotlinx.android.synthetic.main.layout_position_view.view.*

internal class PositionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    private var data: PatrimonyViewData? = null
    private var isAbleToShowMoney = true

    init {
        inflate(context,
            R.layout.layout_position_view, this)
    }

    fun setupState(patrimonyState: PatrimonyViewData) {
        showSuccess(patrimonyState)
    }

    private fun showSuccess(positionData: PatrimonyViewData) {
        textViewMyPatrimony.visible()
        groupPatrimonySuccess.visible()
        setupStateAsSuccess(positionData)
    }

    private fun setupStateAsSuccess(positionData: PatrimonyViewData): Unit = with(positionData) {
        updateMoneyValues(this)
        setupPatrimonyChart(investmentTypes)
        data = this
    }

    private fun updateMoneyValues(position: PatrimonyViewData) = with(position) {
        textViewPatrimonyValue.animateTextChange(prepareMoneyValue(amountPatrimony))
        textViewMyInvestmentsValue.animateTextChange(prepareMoneyValue(amountInvestments))
        textViewEasyAccountValue.animateTextChange(prepareMoneyValue(amountAccount))
    }

    private fun prepareMoneyValue(moneyValue: String): String {
        return if (isAbleToShowMoney) moneyValue else moneyValue.hideMoney()
    }

    private fun setupPatrimonyChart(investmentTypes: List<PieChartView.Value>) {
        val pieChartView = PieChartView(
            context,
            investmentTypes
        ).build()

        frameLayoutPatrimonyGraph.apply {
            removeAllViews()

            addView(
                pieChartView,
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.MATCH_PARENT
            )
        }
    }
}