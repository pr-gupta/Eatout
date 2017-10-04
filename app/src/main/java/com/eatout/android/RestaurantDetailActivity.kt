package com.eatout.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import com.bumptech.glide.Glide
import com.eatout.android.databinding.RestaurantDetailActivityBinding
import com.eatout.android.model.view.RestaurantDetailViewModel
import com.eatout.android.util.GPSUtil
import com.eatout.android.util.NetworkUtil
import com.eatout.android.util.PropertyUtil
import com.eatout.android.util.uber.UberUtil
import com.eatout.android.util.zomato.beans.ZomatoWebViewActivity
import com.eatout.android.util.zomato.beans.restaurant.search.Restaurant_
import com.eatout.android.util.zomato.controller.RestaurantDetailController
import com.eatout.android.util.zomato.controller.RestaurantDetailControllerOffline
import com.eatout.android.util.zomato.events.GetRestaurantDetailCompletionEvent
import com.uber.sdk.android.rides.RideParameters
import com.uber.sdk.android.rides.RideRequestActivity
import com.uber.sdk.rides.client.ServerTokenSession
import com.uber.sdk.rides.client.SessionConfiguration
import com.uber.sdk.rides.client.UberRidesApi
import com.uber.sdk.rides.client.model.PriceEstimatesResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantDetailActivity : Activity(), RestaurantDetailViewModel.RestaurantDetailViewModelChangeListener {

    private lateinit var _resID: String

    private lateinit var _binding: RestaurantDetailActivityBinding
    private lateinit var restaurant: Restaurant_
    private val TAG = RestaurantDetailActivity::class.java.simpleName


    companion object {
        private val RES_ID = "restaurant_id"

        fun newIntent(context: Context, resID: String): Intent {
            val data = Intent(context, RestaurantDetailActivity::class.java)
            data.putExtra(RES_ID, resID)

            return data
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.restaurant_detail_activity)
        _binding.viewModel = RestaurantDetailViewModel(this)

        _resID = intent.extras.getString(RES_ID, null)

        UberUtil.init()

        EventBus.getDefault().register(this)

        if (NetworkUtil.isNetworkAvailable(this))
            RestaurantDetailController(this).getRestaurantDetails(_resID)
        else
            RestaurantDetailControllerOffline(this).getRestaurantDetails(_resID)
    }

    override fun onStart() {
        super.onStart()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    fun onGetRestaurantCompletion(getRestaurantDetailCompletionEvent: GetRestaurantDetailCompletionEvent) {
        restaurant = getRestaurantDetailCompletionEvent.restaurant

        Log.v(TAG, "Restaurant deatils fetch complete")
        Log.v(TAG, restaurant.toString())

        _binding.viewModel.isRestaurantDataLoaded.set(true)
        _binding.viewModel.restaurantName.set(restaurant.name)
        val sourceLocation = Location("source")
        sourceLocation.latitude = GPSUtil._latitude
        sourceLocation.longitude = GPSUtil._longitude

        val destinationLocation = Location("destination")
        destinationLocation.latitude = restaurant.location.latitude.toDouble()
        destinationLocation.longitude = restaurant.location.longitude.toDouble()

        val distance = sourceLocation.distanceTo(destinationLocation)
        _binding.viewModel.restaurantDistance.set(
                when (distance) {
                    in 0..999 -> " ${distance.toInt()} meters"
                    else -> " ${(distance / 1000).toInt()} Km"
                }
        )
        _binding.viewModel.restaurantCost.set("  ${restaurant.averageCostForTwo} ${restaurant.currency}")
        _binding.viewModel.restaurantAddress.set(restaurant.location.address)
        _binding.viewModel.restaurantRating.set(restaurant.userRating.aggregateRating.toFloat())
        _binding.viewModel.restaurantRatingColor.set(Color.parseColor("#${restaurant.userRating.ratingColor}"))

        Glide.with(this).load(restaurant.featuredImage).into(_binding.ivRestaurantThumb)

        _binding.viewModel.restaurantCuisinesText.set(restaurant.cuisines)
        _binding.viewModel.hasOnlineDelivery.set(restaurant.hasOnlineDelivery)
        _binding.viewModel.isDeliveringNow.set(restaurant.isDeliveringNow)
        _binding.viewModel.hasTableBooking.set(restaurant.hasTableBooking)

        loadUberData(restaurant)
    }

    fun loadUberData(restaurant: Restaurant_) {

        val sessionConfiguration = SessionConfiguration.Builder()
                .setClientId(PropertyUtil.getProperty("uberClientID"))
                .setServerToken(PropertyUtil.getProperty("uberServerToken"))
                .build()

        val session = ServerTokenSession(sessionConfiguration)

        UberRidesApi.with(session).build().createService().getPriceEstimates(
                GPSUtil._latitude.toFloat(),
                GPSUtil._longitude.toFloat(),
                restaurant.location.latitude.toFloat(),
                restaurant.location.longitude.toFloat()
        ).enqueue(object : Callback<PriceEstimatesResponse> {

            override fun onResponse(call: Call<PriceEstimatesResponse>?, response: Response<PriceEstimatesResponse>?) {
                val priceList = response!!.body()
                _binding.viewModel.isUberDataLoaded.set(true)
                var cnt = 0
                if (priceList != null) {
                    loop@ for (price in priceList.prices) {
                        cnt++
                        when (cnt) {
                            1 -> _binding.viewModel.UberRideDetails1.set("${price.displayName} : ${price.estimate}")
                            2 -> _binding.viewModel.UberRideDetails2.set("${price.displayName} : ${price.estimate}")
                            3 -> _binding.viewModel.UberRideDetails3.set("${price.displayName} : ${price.estimate}")
                            4 -> _binding.viewModel.UberRideDetails4.set("${price.displayName} : ${price.estimate}")
                            else -> break@loop
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PriceEstimatesResponse>?, t: Throwable?) {
            }
        })
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun buildAndStartUberRideActivity() {
        val rideParameters = RideParameters.Builder()
                .setPickupLocation(GPSUtil._latitude, GPSUtil._longitude, null, null)
                .setDropoffLocation(restaurant.location.latitude.toDouble(), restaurant.location.longitude.toDouble(), restaurant.name, restaurant.location.address)
                .build()
        startActivity(RideRequestActivity.newIntent(this, rideParameters, UberUtil.config, null))
    }

    override fun buildAndStartZomatoWebViewActivity() {
        startActivity(ZomatoWebViewActivity.newIntent(this, restaurant.url))
    }
}
