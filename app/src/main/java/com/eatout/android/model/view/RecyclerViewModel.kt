package com.eatout.android.model.view

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.eatout.android.adapter.RestaurantListAdaptor

/**
 * Created by prashant.gup on 22/09/17.
 */
class RecyclerViewModel(
        val adapter: ObservableField<RestaurantListAdaptor>,
        val layoutManager: ObservableField<GridLayoutManager>
) {

    companion object {
        @BindingAdapter("app:adapter")
        fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: ObservableField<RestaurantListAdaptor>) {
            adapter.get()?.let {
                recyclerView.adapter = adapter.get()
            }
        }

        @BindingAdapter("app:layoutManager")
        fun bindRecylerViewLayoutManager(recyclerView: RecyclerView, layoutManager: ObservableField<GridLayoutManager>) {
            layoutManager.get()?.let {
                recyclerView.layoutManager = layoutManager.get()
            }
        }
    }

    fun scrollToTop() {
        layoutManager.get().scrollToPositionWithOffset(0, 0)
    }
}