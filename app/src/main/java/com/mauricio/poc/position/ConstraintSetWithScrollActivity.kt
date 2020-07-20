package com.mauricio.poc.position

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.data.PatrimonyError
import com.mauricio.poc.position.data.PatrimonyErrorType
import com.mauricio.poc.position.data.PatrimonyLoading
import com.mauricio.poc.position.data.ProfitabilityError
import com.mauricio.poc.position.data.ProfitabilityErrorType
import com.mauricio.poc.position.data.ProfitabilityLoading
import com.mauricio.poc.position.data.Repository
import kotlinx.android.synthetic.main.activity_constraintset_with_scroll.*

class ConstraintSetWithScrollActivity : AppCompatActivity() {
    private var isShowingValues = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraintset_with_scroll)

        setupPatrimonyView()
        setupProfitabilityView()
        setupListeners()
    }

    private fun setupListeners() {
        showHideButton.setOnClickListener {
            if (isShowingValues) {
                patrimonyView.hideMoney()
                profitabilityView.hideMoney()
            } else {
                patrimonyView.showMoney()
                profitabilityView.showMoney()
            }

            isShowingValues = !isShowingValues
        }
        showLoading.setOnClickListener {
            patrimonyView.setupState(PatrimonyLoading)
            profitabilityView.setupState(ProfitabilityLoading)
        }
        showError.setOnClickListener {
            patrimonyView.setupState(
                PatrimonyError(
                    type = PatrimonyErrorType.GENERAL,
                    title = "",
                    description = ""
                )
            )
            profitabilityView.setupState(
                ProfitabilityError(
                    type = ProfitabilityErrorType.GENERAL,
                    title = "",
                    description = ""
                )
            )
        }
        showSuccess.setOnClickListener {
            patrimonyView.setupState(Repository.loadPatrimonyData())
            profitabilityView.setupState(Repository.loadProfitabilityData())
        }
    }

    private fun setupPatrimonyView() = with(patrimonyView) {
        setupState(Repository.loadPatrimonyData())
        setTransitionListener { transition ->
            TransitionManager.beginDelayedTransition(content, transition)
        }
        setTryAgainClickListener {
            patrimonyView.setupState(PatrimonyLoading)
            postDelayed({
                patrimonyView.setupState(Repository.loadPatrimonyData())
            }, 1000)
        }
    }

    private fun setupProfitabilityView() = with(profitabilityView) {
        setupState(Repository.loadProfitabilityData())
        // setTransitionListener { transition ->
        //     TransitionManager.beginDelayedTransition(content, transition)
        // }
        setTryAgainClickListener {
            profitabilityView.setupState(ProfitabilityLoading)
            postDelayed({
                profitabilityView.setupState(Repository.loadProfitabilityData())
            }, 1000)
        }
    }
}