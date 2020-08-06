package com.mauricio.poc.position.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.poc.position.R
import com.mauricio.poc.position.data.MenuAction
import com.mauricio.poc.position.data.MenuActionSize
import com.mauricio.poc.position.extensions.isVisible
import kotlinx.android.synthetic.main.layout_other_menu_view_small.view.*

class ActionsAdapter(
    private val menus: List<MenuAction>,
    private val widthResolver: ActionWidthResolver
) : RecyclerView.Adapter<ActionsAdapter.ActionAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionAdapterViewHolder {
        val isSmallType = viewType == R.layout.layout_other_menu_view_small
        val layoutRes = if (isSmallType) {
            R.layout.layout_other_menu_view_small
        } else {
            R.layout.layout_other_menu_view_big
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false).apply {
            if (isSmallType) widthResolver.applyWidth(view = this)
        }

        return ActionAdapterViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (menus[position].size == MenuActionSize.SMALL) {
            R.layout.layout_other_menu_view_small
        } else {
            R.layout.layout_other_menu_view_big
        }
    }

    override fun getItemCount() = menus.size

    override fun onBindViewHolder(holder: ActionAdapterViewHolder, position: Int) {
        holder.bindView(menus[position])
    }

    class ActionAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(menu: MenuAction) {
            itemView.apply {
                imageViewAction.setImageDrawable(ContextCompat.getDrawable(itemView.context, menu.icon))
                textViewTitleAction.text = menu.title
                textViewDescriptionAction.text = menu.description
                imageViewTag.isVisible(menu.isNew)

                setOnClickListener {
                    menu.action.invoke()
                }
            }
        }
    }
}

