package com.mauricio.poc.position.adapter

import android.content.res.Resources
import android.view.View
import androidx.annotation.DimenRes

class ActionWidthResolver(
    @DimenRes private val horizontalSpaceDimen: Int,
    private val horizontalSpaceCount: Int,
    private val numberOfElementsPerRow: Int
) {

    companion object {
        const val WIDTH_SMALL_TYPE = 0.5f
    }

    fun applyWidth(view: View) {
        val screenWidthSize = Resources.getSystem().displayMetrics.widthPixels
        val horizontalSpace = view.context.resources.getDimension(horizontalSpaceDimen)

        val gapWidth = horizontalSpace * horizontalSpaceCount / numberOfElementsPerRow

        val layoutParams = view.layoutParams.apply {
            width = (screenWidthSize * WIDTH_SMALL_TYPE - gapWidth).toInt()
        }
        view.layoutParams = layoutParams
    }
}