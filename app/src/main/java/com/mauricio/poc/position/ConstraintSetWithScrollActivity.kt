package com.mauricio.poc.position

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.data.PatrimonyLoading
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
    }

    private fun setupPatrimonyView() = with(patrimonyView) {
        setTransitionListener { transition ->
            TransitionManager.beginDelayedTransition(content, transition)
        }
        setupState(Repository.loadPatrimonyData())
        setTryAgainClickListener {
            patrimonyView.setupState(PatrimonyLoading)
            postDelayed({
                patrimonyView.setupState(Repository.loadPatrimonyData())
            }, 1000)
        }
    }

    private fun setupProfitabilityView() = with(profitabilityView) {
        setTransitionListener { transition ->
            TransitionManager.beginDelayedTransition(content, transition)
        }
        setupState(Repository.loadProfitabilityData())
        setTryAgainClickListener {
            profitabilityView.setupState(ProfitabilityLoading)
            postDelayed({
                profitabilityView.setupState(Repository.loadProfitabilityData())
            }, 1000)
        }
    }
}