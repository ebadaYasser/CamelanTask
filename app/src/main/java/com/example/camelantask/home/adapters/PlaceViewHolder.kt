package com.example.camelantask.home.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.camelantask.entities.places.Item
import kotlinx.android.synthetic.main.places_list_item.view.*

class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Item) {
        itemView.apply {
            tvPlaceName.text = item.venue?.name
            tvAddress.text = item.venue?.location?.formattedAddress?.joinToString("-")
        }
    }
}