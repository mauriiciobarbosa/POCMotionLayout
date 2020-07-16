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
import androidx.core.view.isVisible
import com.mauricio.poc.position.R
import com.mauricio.poc.position.data.PatrimonyError
import com.mauricio.poc.position.data.PatrimonyLoading
import com.mauricio.poc.position.data.PatrimonyState
import com.mauricio.poc.position.data.PatrimonyViewData
import com.mauricio.poc.position.extensions.animateTextChange
import com.mauricio.poc.position.extensions.gone
import com.mauricio.poc.position.extensions.hideMoney
import com.mauricio.poc.position.extensions.invisible
import com.mauricio.poc.position.extensions.moneyFormat
import com.mauricio.poc.position.extensions.visible
import kotlinx.android.synthetic.main.layout_generic_error.view.*
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

    fun setupState(patrimonyState: PatrimonyState) {
        if (patrimonyState == data) return

        when (patrimonyState) {
            is PatrimonyLoading -> showLoading()
            is PatrimonyViewData -> showSuccess(patrimonyState)
            is PatrimonyError -> showError(patrimonyState)
        }
    }

    private fun showLoading() {
        progressBarLoading.visible()
        textViewMyPatrimony.gone()
        groupPatrimonySuccess.gone()
        groupSelectedInvestment.gone()
        layoutPositionError.gone()
        animationListener?.invoke(AutoTransition().apply {
            duration = ANIMATION_DURATION
        })
        data = null
    }

    private fun showSuccess(positionData: PatrimonyViewData) {
        textViewMyPatrimony.visible()
        groupPatrimonySuccess.visible()
        groupSelectedInvestment.isVisible = isExpanded
        progressBarLoading.gone()
        layoutPositionError.gone()
        setupStateAsSuccess(positionData)
    }

    private fun setupStateAsSuccess(positionData: PatrimonyViewData): Unit = with(positionData) {
        data = this
        updateMoneyValues(this)
        setupPatrimonyChart(investmentTypes)
        updateSelectedInvestment(pieChartView?.selectedValue)
        setToggleListener()
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
            context.getString(R.string.label_total_invested) to (data?.amountInvestments ?: "")
        }
        textViewInvestmentSelectedLabel.animateTextChange(selectedInvestment.first)
        textViewInvestmentSelectedValue.animateTextChange(prepareMoneyValue(selectedInvestment.second))
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
        val transitionSet = createOpenClosedTransition(rotationValues)
        newConstraintSet.applyTo(contentContainer)

        animationListener?.invoke(transitionSet)
    }

    private fun createOpenClosedTransition(rotationValues: Pair<Float, Float>): TransitionSet {
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

    private fun showError(error: PatrimonyError) {
        textViewMyPatrimony.visible()
        layoutPositionError.visible()
        progressBarLoading.gone()
        groupPatrimonySuccess.gone()
        groupSelectedInvestment.gone()
        setupStateAsError(error)
        animationListener?.invoke(AutoTransition().apply {
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