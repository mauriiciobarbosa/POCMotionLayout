package com.mauricio.poc.position.components

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

private const val HISTORY_START_Y_POSITION = 40f

internal fun createHistoryEnterAnimator(
    title: View,
    history: View
): Animator = AnimatorSet().apply {
    playTogether(
        ObjectAnimator.ofFloat(title, View.ALPHA, 0f, 1f),
        ObjectAnimator.ofFloat(title, View.TRANSLATION_Y, HISTORY_START_Y_POSITION, 0f),
        ObjectAnimator.ofFloat(history, View.ALPHA, 0f, 1f),
        ObjectAnimator.ofFloat(history, View.TRANSLATION_Y, HISTORY_START_Y_POSITION, 0f)
    )
}

internal fun createHistoryExitAnimator(
    title: View,
    history: View,
    listener: () -> Unit
): Animator = AnimatorSet().apply {
    playTogether(
        ObjectAnimator.ofFloat(title, View.ALPHA, 1f, 0f),
        ObjectAnimator.ofFloat(title, View.TRANSLATION_Y, 0f, HISTORY_START_Y_POSITION),
        ObjectAnimator.ofFloat(history, View.ALPHA, 1f, 0f),
        ObjectAnimator.ofFloat(history, View.TRANSLATION_Y, 0f, HISTORY_START_Y_POSITION)
    )
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            // do nothing
        }

        override fun onAnimationEnd(animation: Animator?) {
            listener.invoke()
        }

        override fun onAnimationCancel(animation: Animator?) {
            // do nothing
        }

        override fun onAnimationStart(animation: Animator?) {
            // do nothing
        }
    })
}