package com.mauricio.poc.position.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.poc.position.extensions.dpToPx

class SpaceItemDecoration(
    private val context: Context,
    private val space: Space
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) + 1

        if (position % 2 == 0) {
            configureSpace(outRect, space)
        } else {
            configureSpace(outRect, space.copy(left = null, right = null))
        }
    }

    private fun configureSpace(outRect: Rect, space: Space) {
        val (left, right, top, bottom) = space

        left?.let { outRect.left = context.dpToPx(it) }
        right?.let { outRect.right = context.dpToPx(it) }
        top?.let { outRect.top = context.dpToPx(it) }
        bottom?.let { outRect.bottom = context.dpToPx(it) }
    }

    data class Space(
        @DimenRes val left: Int? = null,
        @DimenRes val right: Int? = null,
        @DimenRes val top: Int? = null,
        @DimenRes val bottom: Int? = null
    )
}