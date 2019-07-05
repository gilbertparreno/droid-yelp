package com.gp.yelp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.gp.yelp.R
import com.gp.yelp.network.model.Reviews
import com.gp.yelp.utils.CircleTransform
import com.gp.yelp.utils.REVIEW_DATE_FORMAT
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_review.view.*
import java.text.SimpleDateFormat

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ItemViewHolder>() {

    private var reviews = mutableListOf<Reviews.Review>()
    private val reviewDateFormat = SimpleDateFormat(REVIEW_DATE_FORMAT)
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy")

    fun addItems(newReviews: List<Reviews.Review>) {
        val oldSize = reviews.size
        reviews.addAll(newReviews)

        val newSize = reviews.size
        notifyItemRangeInserted(oldSize, newSize)
    }

    fun clearAdapter() {
        reviews.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent.inflate(R.layout.item_review, false))
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView


        fun bind(item: Reviews.Review) {
            view.tvUserName.text = item.user.name
            view.tvReviewText.text = item.text
            view.rbReviewRating.rating = item.rating.toFloat()
            Picasso.get().load(item.user.imageUrl).error(R.drawable.ic_default_user)
                    .transform(CircleTransform()).into(view.imgUserProfilePic)
            val reviewDate = reviewDateFormat.parse(item.timeCreated)
            view.tvReviewDate.text = dateFormat.format(reviewDate)
        }
    }
}
