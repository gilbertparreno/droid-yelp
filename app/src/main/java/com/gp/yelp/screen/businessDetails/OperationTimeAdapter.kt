package com.gp.yelp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.gp.yelp.R
import com.gp.yelp.network.model.Business
import kotlinx.android.synthetic.main.item_operation_time.view.*
import java.text.SimpleDateFormat
import java.util.*

class OperationTimeAdapter : RecyclerView.Adapter<OperationTimeAdapter.ItemViewHolder>() {

    private var operatingHours = mutableListOf<Business.OpenItem>()

    private val defaultTimeFormat = SimpleDateFormat("HHmm")
    private val timeFormat = SimpleDateFormat("hh:mm a")

    fun addItems(items: List<Business.OpenItem>) {
        val oldSize = operatingHours.size
        operatingHours.addAll(items)

        val newSize = operatingHours.size
        notifyItemRangeInserted(oldSize, newSize)
    }

    fun clearAdapter() {
        operatingHours.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent.inflate(R.layout.item_operation_time, false))
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = operatingHours.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(operatingHours[position])
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView


        fun bind(item: Business.OpenItem) {
            val startTime = defaultTimeFormat.parse(item.start)
            val endTime = defaultTimeFormat.parse(item.end)
            view.tvOperationTime.text = "${getDay(item.day)} ${timeFormat.format(startTime)} - ${timeFormat.format(endTime)}"
        }
    }

    private fun getDay(day: Int) : String = when (day) {
        0 -> "Monday"
        1 -> "Tuesday"
        2 -> "Wednesday"
        3 -> "Thursday"
        4 -> "Friday"
        5 -> "Saturday"
        else -> "Sunday"
    }
}
