package com.mauricio.poc.position

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.poc.position.data.PatrimonyRepository
import kotlinx.android.synthetic.main.activity_motio_with_scroll.*

class MotionWithScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motio_with_scroll)

        setupPatrimonyView()
    }

    private fun setupPatrimonyView() {
        patrimonyView.setupState(PatrimonyRepository.loadData())
    }
}