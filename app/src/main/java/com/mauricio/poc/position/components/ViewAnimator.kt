package com.mauricio.poc.position.components

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

private const val START_Y_POSITION = 40f

internal fun startEnterAnimator(
    vararg views: View,
    listener: (() -> Unit)? = null
) {
    val animators = views.flatMap { view ->
        listOf(
            ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f),
            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, START_Y_POSITION, 0f)
        )
    }
    val animatorSet = AnimatorSet().apply {
        playTogether(animators)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // do nothing
            }

            override fun onAnimationEnd(animation: Animator?) {
                listener?.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {
                // do nothing
            }

            override fun onAnimationStart(animation: Animator?) {
                // do nothing
            }
        })
    }
    animatorSet.start()
}

internal fun startExitAnimator(
    vararg views: View,
    listener: (() -> Unit)? = null
) {
    val animators = views.flatMap { view ->
        listOf(
            ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f),
            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f, START_Y_POSITION)
        )
    }
    val animatorSet = AnimatorSet().apply {
        playTogether(animators)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // do nothing
            }

            override fun onAnimationEnd(animation: Animator?) {
                listener?.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {
                // do nothing
            }

            override fun onAnimationStart(animation: Animator?) {
                // do nothing
            }
        })
    }
    animatorSet.start()
}