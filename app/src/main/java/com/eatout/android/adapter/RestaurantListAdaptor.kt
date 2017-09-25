package com.eatout.android.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.eatout.android.R
import com.eatout.android.RestaurantDetailActivity
import com.eatout.android.databinding.RestaurantCardBinding
import com.eatout.android.model.view.RestaurantCardViewModel
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult


/**
 * Created by prashant.gup on 01/09/17.
 *
 */
class RestaurantListAdaptor(
        val _context: Context,
        private var _searchResult: SearchResult,
        private val onReachedToLastItem: (Int) -> Unit
) : RecyclerView.Adapter<RestaurantListAdaptor.RestaurantListViewHolder>() {

    private val TAG = RestaurantListAdaptor::class.java.simpleName

    override fun getItemCount() = _searchResult.restaurants.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RestaurantListViewHolder?, position: Int) {
        val restaurant = _searchResult.restaurants[position].restaurant
        holder!!.bind.restaurantCardViewModel.restaurantTitle.set(restaurant.name)
        holder.bind.restaurantCardViewModel.restaurantCost.set("${restaurant.averageCostForTwo} ${restaurant.currency}")
        holder.bind.restaurantCardViewModel.restaurantRatingValue.set(restaurant.userRating.aggregateRating.toFloat())
        holder.bind.restaurantCardViewModel.restaurantRatingColor.set(Color.parseColor("#${restaurant.userRating.ratingColor}"))

        Glide.with(_context).load(restaurant.thumb).into(holder.bind.restaurantThumbnail)

        holder.bind.overflow.setOnClickListener({ showPopupMenu(holder.bind.overflow) })

        holder.bind.restaurantThumbnail.setOnClickListener({
            Log.v(TAG, "view clicked!")
            _context.startActivity(RestaurantDetailActivity.newIntent(_context, restaurant.r.resId.toString()))
        })

        if(position == itemCount-1) {
            onReachedToLastItem(position+1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RestaurantListViewHolder {

        val binding = RestaurantCardBinding.inflate(LayoutInflater.from(_context), parent, false)
        binding.restaurantCardViewModel = RestaurantCardViewModel()
        return RestaurantListViewHolder(binding)
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(_context, view)

        popup.menuInflater.inflate(R.menu.restaurant_more_menu, popup.menu)
        popup.setOnMenuItemClickListener(RestaurantMenuItemClickListener())
        popup.show()
    }

    class RestaurantListViewHolder(binding: RestaurantCardBinding) : RecyclerView.ViewHolder(binding.root) {

        private val TAG = javaClass.simpleName
        var bind:RestaurantCardBinding = binding

        init {
            binding.root.isClickable = true
        }

    }

    internal inner class RestaurantMenuItemClickListener : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.action_add_favourite -> {
                    Toast.makeText(_context, "Add to favourite", Toast.LENGTH_SHORT).show()
                    return true
                }
                R.id.action_dont_show -> {
                    Toast.makeText(_context, "Don't show again", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
            return false
        }
    }

}