package com.mauricio.poc.position.components

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.mauricio.poc.position.R
import com.mauricio.poc.position.data.ProfitabilityError
import com.mauricio.poc.position.data.ProfitabilityLoading
import com.mauricio.poc.position.data.ProfitabilityNoContent
import com.mauricio.poc.position.data.ProfitabilityState
import com.mauricio.poc.position.data.SummaryProfitabilityViewData
import com.mauricio.poc.position.extensions.animateTextChange
import com.mauricio.poc.position.extensions.gone
import com.mauricio.poc.position.extensions.hideMoney
import com.mauricio.poc.position.extensions.invisible
import com.mauricio.poc.position.extensions.isVisible
import com.mauricio.poc.position.extensions.setMoneyColor
import com.mauricio.poc.position.extensions.visible
import kotlinx.android.synthetic.main.layout_generic_error.view.*
import kotlinx.android.synthetic.main.layout_profitability_view.view.*
import kotlinx.android.synthetic.main.layout_profitability_view_no_content.view.*
import kotlinx.android.synthetic.main.layout_profitability_view_successful.view.*

internal class ProfitabilityView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var data: SummaryProfitabilityViewData? = null
    private var isAbleToShowMoney = false

    init {
        inflate(context, R.layout.layout_profitability_view, this)
    }

    fun setupState(profitabilityState: ProfitabilityState) {
        if (data == profitabilityState) return

        when (profitabilityState) {
            is ProfitabilityLoading -> showLoading()
            is SummaryProfitabilityViewData -> showSuccess(profitabilityState)
            is ProfitabilityError -> showError(profitabilityState)
            is ProfitabilityNoContent -> showNoContent(profitabilityState)
        }
    }

    private fun showLoading() {
        progressBarLoading.visible()
        textViewProfitabilityTitle.invisible()
        layoutProfitabilitySuccessful.invisible()
        layoutProfitabilityError.gone()
        layoutProfitabilityNoContent.gone()
    }

    private fun showSuccess(profitabilityState: SummaryProfitabilityViewData) {
        textViewProfitabilityTitle.visible()
        layoutProfitabilitySuccessful.visible()
        layoutProfitabilityError.gone()
        progressBarLoading.gone()
        layoutProfitabilityNoContent.gone()
        setupStateAsSuccess(profitabilityState)
    }

    private fun setupStateAsSuccess(profitabilityData: SummaryProfitabilityViewData) =
        with(profitabilityData) {
            textViewProfitabilityDate.text = date
            textViewProfitabilityPeriod.text = period
            textViewProfitabilityValue.text = if (isAbleToShowMoney) value else value.hideMoney()
            textViewProfitabilityValue.setMoneyColor(
                colorPositive = R.color.dark_rise,
                colorNegative = R.color.nightfall
            )
            textViewProfitabilityPercentage.text = percentage
            textViewProfitabilityPercentage.setMoneyColor(
                colorPositive = R.color.dark_rise,
                colorNegative = R.color.nightfall
            )
            data = this
        }

    private fun showError(profitabilityState: ProfitabilityError) {
        textViewProfitabilityTitle.visible()
        layoutProfitabilityError.visible()
        layoutProfitabilitySuccessful.gone()
        progressBarLoading.gone()
        layoutProfitabilityNoContent.gone()
        setupStateAsError(profitabilityState)
    }

    private fun setupStateAsError(profitabilityError: ProfitabilityError) {
        if (profitabilityError.isGeneral()) {
            setupStateAsGeneralError()
        } else {
            setupStateAsSpecificError(profitabilityError)
        }
    }

    private fun setupStateAsGeneralError() = with(context) {
        textViewTitle.text = getString(R.string.home_general_error_title)
        textViewDescription.text = getString(R.string.home_general_error_description)
        gregButtonRetry.visible()
    }

    private fun setupStateAsSpecificError(profitabilityError: ProfitabilityError) = with(context) {
        textViewTitle.text = profitabilityError.title
        textViewDescription.text = profitabilityError.description
        gregButtonRetry.isVisible(profitabilityError.isInMaintenance().not())
    }

    private fun showNoContent(profitabilityState: ProfitabilityNoContent) {
        textViewProfitabilityTitle.visible()
        layoutProfitabilityNoContent.visible()
        layoutProfitabilitySuccessful.gone()
        layoutProfitabilityError.gone()
        progressBarLoading.gone()
        setupProfitabilityNoContent(profitabilityState)
    }

    private fun setupProfitabilityNoContent(profitabilityState: ProfitabilityNoContent) {
        textViewMessage.text = profitabilityState.message
    }

    fun setLinkClickListener(listener: () -> Unit) {
        textViewProfitabilityLink.setOnClickListener {
            listener()
        }
    }

    fun setTryAgainClickListener(listener: () -> Unit) {
        gregButtonRetry.setOnClickListener {
            listener.invoke()
        }
    }

    fun setContinueInvestingListener(listener: () -> Unit) {
        gregButtonContinueInvesting.setOnClickListener {
            listener.invoke()
        }
    }

    fun showMoney() {
        val profitabilityValue = data?.value.orEmpty()

        textViewProfitabilityValue.animateTextChange(profitabilityValue)

        isAbleToShowMoney = true
    }

    fun hideMoney() {
        textViewProfitabilityValue.animateTextChange(data?.value.orEmpty().hideMoney())

        isAbleToShowMoney = false
    }
}