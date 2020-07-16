package com.mauricio.poc.position

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.data.PatrimonyRepository
import kotlinx.android.synthetic.main.activity_constraintset_with_scroll.*

class ConstraintSetWithScrollActivity : AppCompatActivity() {
    private var isShowingValues = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraintset_with_scroll)

        setupPatrimonyView()
        setupListeners()
    }

    private fun setupListeners() {
        showHideButton.setOnClickListener {
            if (isShowingValues) patrimonyView.hideMoney() else patrimonyView.showMoney()

            isShowingValues = !isShowingValues
        }
    }

    private fun setupPatrimonyView() = with(patrimonyView) {
        setupState(PatrimonyRepository.loadData())
        setTransitionListener { transition ->
            TransitionManager.beginDelayedTransition(content, transition)
        }
    }
}