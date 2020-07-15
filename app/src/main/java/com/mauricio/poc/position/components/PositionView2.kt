package com.mauricio.poc.position.components

import android.animation.ObjectAnimator
import android.content.Context
import android.transition.ArcMotion
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionSet
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.doOnEnd
import androidx.core.transition.doOnStart
import com.mauricio.poc.position.R
import com.mauricio.poc.position.data.PatrimonyViewData
import com.mauricio.poc.position.extensions.animateTextChange
import com.mauricio.poc.position.extensions.hideMoney
import com.mauricio.poc.position.extensions.moneyFormat
import com.mauricio.poc.position.extensions.visible
import kotlinx.android.synthetic.main.layout_position_view2_start.view.*

private const val ANIMATION_DURATION = 500L

internal class PositionView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var data: PatrimonyViewData? = null
    private var isAbleToShowMoney = true
    private var isExpanded = false
    private var animationListener: ((Transition) -> Unit)? = null
    private var pieChartView: PieChartView? = null

    init {
        inflate(context, R.layout.layout_position_view2_start, this)
    }

    fun setupState(patrimonyState: PatrimonyViewData) {
        showSuccess(patrimonyState)
        setToggleListener()
    }

    private fun setToggleListener() {
        imageViewExpandable.setOnClickListener {
            if (isExpanded) {
                updateConstraintSet(R.layout.layout_position_view2_start)
            } else {
                updateConstraintSet(R.layout.layout_position_view2_end)
            }
        }
    }

    private fun updateConstraintSet(@LayoutRes constrainSetId: Int) {
        val rotationValues = if (isExpanded) -180F to 0F else 0F to -180F
        val newConstraintSet = ConstraintSet().apply {
            clone(context, constrainSetId)
        }
        val transitionSet = createTransition(rotationValues)
        newConstraintSet.applyTo(contentContainer)

        animationListener?.invoke(transitionSet)
    }

    private fun createTransition(rotationValues: Pair<Float, Float>): TransitionSet {
        return TransitionSet().apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addTransition(ChangeBounds())
            pathMotion = ArcMotion()
            doOnStart {
                ObjectAnimator.ofFloat(
                    imageViewExpandable, View.ROTATION, rotationValues.first, rotationValues.second
                ).apply {
                    addUpdateListener { valueAnimator ->
                        if (isExpanded.not() && valueAnimator.animatedFraction > 0.9) {
                            pieChartView?.showCenterText()
                        } else {
                            pieChartView?.hideCenterText()
                        }
                    }
                    duration = it.duration
                }.start()
                imageViewExpandable.isEnabled = false
            }
            doOnEnd {
                imageViewExpandable.isEnabled = true
                isExpanded = !isExpanded
            }
        }
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
        textViewInvestmentSelectedLabel.text = context.getString(R.string.label_total_invested)
        textViewInvestmentSelectedValue.text = prepareMoneyValue(amountInvestments)
    }

    private fun prepareMoneyValue(moneyValue: String): String {
        return if (isAbleToShowMoney) moneyValue else moneyValue.hideMoney()
    }

    private fun setupPatrimonyChart(investmentTypes: List<PieChartView.Value>) {
        pieChartView = PieChartView(
            context, investmentTypes
        ).build { valueSelected ->
            val selectedInvestment = if (valueSelected != null) {
                valueSelected.description to valueSelected.value.moneyFormat
            } else {
                context.getString(R.string.label_total_invested) to (data?.amountInvestments ?: "")
            }
            textViewInvestmentSelectedLabel.animateTextChange(selectedInvestment.first)
            textViewInvestmentSelectedValue.animateTextChange(prepareMoneyValue(selectedInvestment.second))
        }.also {
            if (isExpanded) it.showCenterText()
        }

        frameLayoutPatrimonyGraph.apply {
            removeAllViews()

            addView(
                pieChartView,
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun setTransitionListener(animationListener: (Transition) -> Unit) {
        this.animationListener = animationListener
    }
}