package com.eatout.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.eatout.android.R
import com.eatout.android.util.zomato.beans.restaurant.search.Restaurant
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult
import java.util.*
import com.eatout.android.R.id.overflow




/**
 * Created by prashant.gup on 01/09/17.
 */
class RestaurantListAdaptor(val _context: Context, private var _searchResult: SearchResult) : RecyclerView.Adapter<RestaurantListAdaptor.RestaurantListViewHolder>() {

    private val TAG = RestaurantListAdaptor::class.java.simpleName

    override fun getItemCount(): Int {
        return _searchResult.restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantListViewHolder?, position: Int) {
        val restaurant = _searchResult.restaurants[position].restaurant
        holder!!._title.text = restaurant.name
        holder._description.text =  "${restaurant.averageCostForTwo} ${restaurant.currency}"
        holder._rating.rating = restaurant.userRating.aggregateRating.toFloat()
        Glide.with(_context).load(restaurant.thumb).into(holder._thumbnail)

        holder._overflow.setOnClickListener({ showPopupMenu(holder._overflow) })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RestaurantListViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.restaurant_card, parent, false)
        return RestaurantListViewHolder(itemView)
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(_context, view)

        popup.menuInflater.inflate(R.menu.restaurant_more_menu, popup.menu)
        popup.setOnMenuItemClickListener(RestaurantMenuItemClickListener())
        popup.show()
    }

    class RestaurantListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val _title: TextView = view.findViewById(R.id.tv_restaurant_title)
        val _description: TextView = view.findViewById(R.id.tv_restaurant_description)
        val _thumbnail: ImageView = view.findViewById(R.id.iv_restaurant_thumbnail)
        val _rating: RatingBar = view.findViewById(R.id.rb_restaurant_rating)
        val _overflow: ImageView = view.findViewById(R.id.overflow)

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