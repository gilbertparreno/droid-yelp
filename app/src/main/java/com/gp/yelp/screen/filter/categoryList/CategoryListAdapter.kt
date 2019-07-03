package com.gp.yelp.screen.filter.categoryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.gp.yelp.R
import com.gp.yelp.network.model.Category
import kotlinx.android.synthetic.main.dialog_fragment_category.view.*
import kotlinx.android.synthetic.main.item_add_category.view.*
import kotlinx.android.synthetic.main.item_category.view.*
import timber.log.Timber


class CategoryListAdapter : RecyclerView.Adapter<CategoryListAdapter.ItemViewHolder>() {

    private var rawCategories = mutableListOf<Category>()
    private var categories = mutableListOf<Category>()

    var selectedCategories = mutableListOf<Category>()

    fun addItems(list: List<Category>) {
        val oldSize = categories.size
        categories.addAll(list)
        notifyItemRangeInserted(oldSize, categories.size)

        if (rawCategories.isEmpty()) {
            rawCategories.addAll(list)
        }
    }

    fun searchCategory(term: String) {
        val filteredCategory = mutableListOf<Category>()
        if (term.isEmpty()) {
            filteredCategory.addAll(rawCategories)
        } else {
            filteredCategory.addAll(rawCategories.filter {
                it.title.startsWith(term, true)
            })
            // remove duplicates
            val joinedCategories = filteredCategory.union(selectedCategories)
            filteredCategory.clear()
            filteredCategory.addAll(joinedCategories)
        }

        categories.clear()
        notifyDataSetChanged()
        addItems(filteredCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent.inflate(R.layout.item_add_category, false))
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view = itemView

        fun bind(item: Category) {
            view.checkbox.text = item.title

            view.checkbox.isChecked = selectedCategories.indexOf(item) >= 0

            view.checkbox.setOnCheckedChangeListener { _, b ->
                if (!view.checkbox.isPressed) return@setOnCheckedChangeListener
                if (b) {
                    val added = selectedCategories.add(item)
                    Timber.d("item added: $added")
                } else {
                    val isRemoved = selectedCategories.remove(item)
                    Timber.d("item removed: $isRemoved")
                }
            }
        }
    }
}
