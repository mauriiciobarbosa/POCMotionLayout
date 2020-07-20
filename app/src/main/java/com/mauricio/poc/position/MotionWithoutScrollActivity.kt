package com.mauricio.poc.position

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.data.Repository
import kotlinx.android.synthetic.main.activity_motion_without_scroll.*

class MotionWithoutScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_without_scroll)

        setupPatrimonyView()
    }

    private fun setupPatrimonyView() {
        patrimonyView.setupState(Repository.loadPatrimonyData())
    }
}