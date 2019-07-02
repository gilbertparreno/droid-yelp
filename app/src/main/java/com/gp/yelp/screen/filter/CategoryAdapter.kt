package com.gp.yelp.screen.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.gp.yelp.R
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(val categoryListener: CategoryListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface CategoryListener {
        fun addCategory()
    }

    private val ITEM = 0
    private val ADD = 1

    private var categories = mutableListOf<String>()

    fun addItems(newCategory: String) {
        val oldSize = categories.size
        categories.add(0, newCategory)

        val newSize = categories.size
        notifyItemRangeInserted(oldSize, newSize)
    }

    fun clearAdapter() {
        categories.clear()
        notifyDataSetChanged()
    }

    private fun removeItem(item: String) {
        val index = categories.indexOf(item)
        categories.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemViewType(position: Int): Int {
        return if (categories[position].isEmpty()) ADD else ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            ItemViewHolder(parent.inflate(R.layout.item_category, false))
        } else {
            ItemAddViewHolder(parent.inflate(R.layout.item_category_add, false))
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM) {
            holder as ItemViewHolder
            holder.bind(categories[position])
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView

        fun bind(item: String) {
            view.tvCategory.text = item
            view.setOnClickListener {
                removeItem(item)
            }
        }
    }

    inner class ItemAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView

        init {
            view.setOnClickListener {
                categoryListener.addCategory()
            }
        }
    }
}
