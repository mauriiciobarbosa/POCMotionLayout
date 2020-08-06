package com.mauricio.poc.position.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.mauricio.poc.position.R
import com.mauricio.poc.position.extensions.isVisible
import kotlinx.android.synthetic.main.layout_other_menu_view_small.view.*

internal class OtherMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.layout_other_menu_view_small, this)

        val typedArray = context
            .obtainStyledAttributes(attrs, R.styleable.OtherMenuView, defStyleAttr, defStyleRes)

        setup(typedArray)
    }

    private fun setup(typedArray: TypedArray) = with(typedArray) {
        val icon = getResourceId(R.styleable.OtherMenuView_other_menu_icon, -1)
        val title = getString(R.styleable.OtherMenuView_other_menu_title)
        val description = getString(R.styleable.OtherMenuView_other_menu_description)
        val isNew = getBoolean(R.styleable.OtherMenuView_other_menu_isNew, false)

        imageViewAction.setImageResource(icon)
        textViewTitleAction.text = title
        textViewDescriptionAction.text = description
        imageViewTag.isVisible(isNew)

        recycle()
    }

    fun setTitle(text: String) {
        textViewTitleAction.text = text
    }

    fun setDescription(text: String) {
        textViewDescriptionAction.text = text
    }

    fun setAsBig() {
        bgAction.minHeight = resources.getDimension(R.dimen.home_other_menu_height_big).toInt()
        val constraintSet = ConstraintSet().apply {
            load(context, R.layout.layout_other_menu_view_big)
        }
        constraintSet.applyTo(bgAction)
    }

    fun setAsSmall() {
        bgAction.minHeight = resources.getDimension(R.dimen.home_other_menu_height).toInt()
        val constraintSet = ConstraintSet().apply {
            load(context, R.layout.layout_other_menu_view_small)
        }
        constraintSet.applyTo(bgAction)
    }
}