package com.mauricio.poc.position

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
    }

    private fun setupView() {
        motionWithoutScroll.setOnClickListener {
            startActivity(Intent(this, MotionWithoutScrollActivity::class.java))
        }
        motionWithScroll.setOnClickListener {
            startActivity(Intent(this, MotionWithScrollActivity::class.java))
        }
        motionWithScrollAndMatchConstraints.setOnClickListener {
            startActivity(Intent(this, MotionWithScrollAndMatchConstraintsActivity::class.java))
        }
        constraintsetWithScroll.setOnClickListener {
            startActivity(Intent(this, ConstraintSetWithScrollActivity::class.java))
        }
    }
}