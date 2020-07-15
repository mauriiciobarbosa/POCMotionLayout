package com.mauricio.poc.position.extensions

import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.AnimRes
import com.mauricio.poc.position.R

fun TextView.animateTextChange(text: String, @AnimRes animation: Int = R.anim.fade_in_fast) {
    val anim = AnimationUtils.loadAnimation(context, animation)
    this.text = text
    this.startAnimation(anim)
}