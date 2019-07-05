package com.gp.yelp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.gp.yelp.R
import com.gp.yelp.network.model.Business
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_business.view.*

class BusinessAdapter(val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(businessesItem: Business)
    }

    private val ITEM = 0
    private val PROGRESS = 1

    private var businessItems = mutableListOf<Business>()

    fun addItems(newBusinessListItems: List<Business>) {
        val oldSize = businessItems.size
        businessItems.addAll(newBusinessListItems)

        val newSize = businessItems.size
        notifyItemRangeInserted(oldSize, newSize)
    }

    fun clearAdapter() {
        businessItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(parent.inflate(R.layout.item_business, false))
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = businessItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM) {
            holder as ItemViewHolder
            holder.bind(businessItems[position])
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView


        fun bind(item: Business) {

            val distance = (item.distance / 1000)
            var categories: String = item.categories?.joinToString(separator = ", ") {
                it.title
            } ?: ""

            view.tvBusinessName.text = item.name
            view.rbRating.rating = item.rating
            view.tvReviews.text =
                view.context.resources.getQuantityString(
                    R.plurals.lbl_quantity_reviews_1,
                    item.reviewCount,
                    item.reviewCount
                )
            view.tvDistance.text = view.resources.getString(R.string.lbl_business_distance, distance)
            view.tvCategories.text = categories

            if (item.imageUrl.isNotEmpty()) {
                Picasso.get().load(item.imageUrl).resize(640, 360).centerCrop().into(view.imgBusiness)
            }

            view.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }
}