package com.mauricio.poc.position

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.data.PatrimonyRepository
import kotlinx.android.synthetic.main.activity_constraintset_with_scroll.*

class ConstraintSetWithScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraintset_with_scroll)

        setupPatrimonyView()
    }

    private fun setupPatrimonyView() = with(patrimonyView) {
        setupState(PatrimonyRepository.loadData())
        setTransitionListener { transition ->
            TransitionManager.beginDelayedTransition(content, transition)
        }
    }
}