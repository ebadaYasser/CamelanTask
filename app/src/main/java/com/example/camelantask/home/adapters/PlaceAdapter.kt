package com.example.camelantask.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.camelantask.R
import com.example.camelantask.entities.places.Item

class PlaceAdapter : RecyclerView.Adapter<PlaceViewHolder>() {
    val items: ArrayList<Item> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder =
        PlaceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.places_list_item, parent, false))


    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun addItems(newItems: List<Item>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}