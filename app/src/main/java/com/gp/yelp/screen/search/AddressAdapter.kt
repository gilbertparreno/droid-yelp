package com.gp.yelp.screen.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gp.yelp.R
import kotlinx.android.synthetic.main.item_address.view.*
import com.gp.yelp.network.model.Address


class AddressAdapter(val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<AddressAdapter.ItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(address: Address)
    }

    var addresses = mutableListOf<Address>()
    var selected: Address? = null

    fun addItems(newAddresses: List<Address>) {
        if (selected != null) {
            val selectedIndex = addresses.indexOf(selected!!)
            if (selectedIndex >= 0) {
                addresses.removeAt(selectedIndex)
                notifyItemRemoved(selectedIndex)
            }
        }

        val oldSize = addresses.size
        addresses.addAll(newAddresses)
        notifyItemRangeInserted(oldSize, addresses.size)
    }

    private fun onSelectItem(address: Address) {
        selected = address
        onItemClickListener.onItemClick(address)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        addresses.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent.inflate(R.layout.item_address, false))
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = addresses.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView

        fun bind(item: Address) {
            view.tvAddress.text = item.subText
            view.tvName.text = item.name

            view.setOnClickListener {
                onSelectItem(item)
            }

            if (selected != null && item == selected) {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorPrimary30))
            } else {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.bgDefault))
            }
        }
    }
}
