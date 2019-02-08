package com.teamtwothree.kartasvalokapp.presentation.results.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtwothree.kartasvalokapp.model.point.PointDetails

class DataBindingAdapters {

    @BindingAdapter("items")
    internal fun RecyclerView.bindFeedEntries(items: List<PointDetails>?) {
        if (items != null && items.isNotEmpty()) {
            val adapter = adapter as ReportsAdapter
            adapter.items = items
            adapter.notifyDataSetChanged()
        }
    }
}