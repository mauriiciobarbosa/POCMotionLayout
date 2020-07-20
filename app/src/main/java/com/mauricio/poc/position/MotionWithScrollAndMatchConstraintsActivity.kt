package com.mauricio.poc.position

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.data.Repository
import kotlinx.android.synthetic.main.activity_motion_with_scroll.*

class MotionWithScrollAndMatchConstraintsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_with_scroll_and_match_constraints)

        setupPatrimonyView()
    }

    private fun setupPatrimonyView() {
        patrimonyView.setupState(Repository.loadPatrimonyData())
    }
}