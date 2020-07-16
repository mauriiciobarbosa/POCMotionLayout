package com.mauricio.poc.position.components

import android.animation.ObjectAnimator
import android.content.Context
import android.transition.ArcMotion
import android.transition.AutoTransition
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
import com.mauricio.poc.position.data.PatrimonyError
import com.mauricio.poc.position.data.PatrimonyLoading
import com.mauricio.poc.position.data.PatrimonyState
import com.mauricio.poc.position.data.PatrimonyViewData
import com.mauricio.poc.position.extensions.animateTextChange
import com.mauricio.poc.position.extensions.hideMoney
import com.mauricio.poc.position.extensions.moneyFormat
import com.mauricio.poc.position.extensions.visible
import kotlinx.android.synthetic.main.layout_generic_error.view.*
import kotlinx.android.synthetic.main.layout_position_view2_success_collapsed.view.*

private const val ANIMATION_DURATION = 500L
private const val TOGGLE_ROTATE_START = 0F
private const val TOGGLE_ROTATE_END = -180F

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
        inflate(context, R.layout.layout_position_view2_success_collapsed, this)
    }

    fun setupState(patrimonyState: PatrimonyState) {
        if (patrimonyState == data) return

        when (patrimonyState) {
            is PatrimonyLoading -> showLoading()
            is PatrimonyViewData -> showSuccess(patrimonyState)
            is PatrimonyError -> showError(patrimonyState)
        }
    }

    private fun showLoading() {
        updateConstraintSet(R.layout.layout_position_view2_loading, AutoTransition().apply {
            duration = ANIMATION_DURATION
        })
        data = null
    }

    private fun showSuccess(positionData: PatrimonyViewData) {
        setupStateAsSuccess(positionData)
        toggleSuccessConstraintSet(isInitialState = true)
    }

    private fun toggleSuccessConstraintSet(isInitialState: Boolean = false) {
        val (constraintSet, transition) = when {
            isInitialState -> {
                pieChartView?.setNoValueSelected()
                setSelectedInvestmentAsDefault()
                isExpanded = false
                pieChartView?.hideCenterText()
                R.layout.layout_position_view2_success_collapsed to AutoTransition().apply {
                    duration = ANIMATION_DURATION
                }
            }
            isExpanded -> {
                pieChartView?.setNoValueSelected()
                R.layout.layout_position_view2_success_collapsed to createOpenClosedTransition(
                    TOGGLE_ROTATE_END to TOGGLE_ROTATE_START
                )
            }
            else -> R.layout.layout_position_view2_success_expanded to createOpenClosedTransition(
                TOGGLE_ROTATE_START to TOGGLE_ROTATE_END
            )
        }

        updateConstraintSet(constraintSet, transition)
    }

    private fun setSelectedInvestmentAsDefault() {
        textViewInvestmentSelectedLabel.text = context.getString(R.string.label_total_invested)
        textViewInvestmentSelectedValue.text = data?.amountInvestments.orEmpty()
    }

    private fun setupStateAsSuccess(positionData: PatrimonyViewData): Unit = with(positionData) {
        data = this
        updateMoneyValues(this)
        setupPatrimonyChart(investmentTypes)
        updateSelectedInvestment(pieChartView?.selectedValue)
        imageViewExpandable.setOnClickListener { toggleSuccessConstraintSet() }
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
        pieChartView = PieChartView(
            context, investmentTypes
        ).build { valueSelected ->
            updateSelectedInvestment(valueSelected)
            if (valueSelected != null && isExpanded.not()) {
                toggleSuccessConstraintSet()
            }
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

    private fun updateSelectedInvestment(valueSelected: PieChartView.Value?) {
        val selectedInvestment = if (valueSelected != null) {
            valueSelected.description to valueSelected.value.moneyFormat
        } else {
            context.getString(R.string.label_total_invested) to data?.amountInvestments.orEmpty()
        }
        textViewInvestmentSelectedLabel.animateTextChange(selectedInvestment.first)
        textViewInvestmentSelectedValue.animateTextChange(prepareMoneyValue(selectedInvestment.second))
    }

    private fun updateConstraintSet(@LayoutRes constrainSetId: Int, transition: Transition) {
        val newConstraintSet = ConstraintSet().apply {
            clone(context, constrainSetId)
        }
        newConstraintSet.applyTo(contentContainer)

        animationListener?.invoke(transition)
    }

    private fun createOpenClosedTransition(rotationValues: Pair<Float, Float>) =
        TransitionSet().apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addTransition(ChangeBounds())
            pathMotion = ArcMotion()
            doOnStart {
                ObjectAnimator.ofFloat(
                    imageViewExpandable,
                    View.ROTATION,
                    rotationValues.first,
                    rotationValues.second
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
                if (isExpanded.not()) setSelectedInvestmentAsDefault()
            }
        }

    private fun showError(error: PatrimonyError) {
        setupStateAsError(error)
        updateConstraintSet(R.layout.layout_position_view2_error, AutoTransition().apply {
            duration = ANIMATION_DURATION
        })
        data = null
    }

    private fun setupStateAsError(error: PatrimonyError) {
        if (error.isGeneral()) {
            setupStateAsGeneralError()
        } else {
            setupStateAsSpecificError(error)
        }
    }

    private fun setupStateAsGeneralError() = with(context) {
        textViewTitle.text = getString(R.string.home_general_error_title)
        textViewDescription.text = getString(R.string.home_general_error_description)
        gregButtonRetry.visible()
    }

    private fun setupStateAsSpecificError(error: PatrimonyError) = with(context) {
        textViewTitle.text = error.title
        textViewDescription.text = error.description
    }

    fun setTransitionListener(animationListener: (Transition) -> Unit) {
        this.animationListener = animationListener
    }

    fun setOnLinkClickListener(listener: () -> Unit) {
        textViewMyInvestmentsLink.setOnClickListener {
            listener()
        }
    }

    fun setTryAgainClickListener(listener: () -> Unit) {
        gregButtonRetry.setOnClickListener {
            listener.invoke()
        }
    }

    fun showMoney() {
        isAbleToShowMoney = true

        if (isSuccessState()) {
            val position = data ?: return
            updateMoneyValues(position)
            updateSelectedInvestment(pieChartView?.selectedValue)
        }
    }

    private fun isSuccessState(): Boolean {
        return data != null && groupPatrimonySuccess.visibility == View.VISIBLE
    }

    fun hideMoney() {
        isAbleToShowMoney = false

        if (isSuccessState()) {
            val position = data ?: return
            updateMoneyValues(position)
            updateSelectedInvestment(pieChartView?.selectedValue)
        }
    }
}