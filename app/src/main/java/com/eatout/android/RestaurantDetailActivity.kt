package com.eatout.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
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
import com.uber.sdk.rides.client.model.ProductsResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantDetailActivity : Activity() {

    private lateinit var _uberRideButton: Button
    private lateinit var _pbRestaurantWait: ProgressBar
    private lateinit var _resID: String
    private lateinit var _tvRestaurantName: TextView
    private lateinit var _tvRestaurantAddesss: TextView
    private lateinit var _tvRestaurantCost: TextView
    private lateinit var _tvRestaurantDistance: TextView
    private lateinit var _rbRestaurantRating: RatingBar
    private lateinit var _ivRestaurantImage: ImageView
    private lateinit var _tvRestaurantCuisines: TextView
    private lateinit var _llRestaurantDetails: LinearLayout
    private lateinit var _tvHasOnlineDelivery: TextView
    private lateinit var _tvIsDeliveringNow: TextView
    private lateinit var _tvHasTableBooking: TextView
    private lateinit var _pbUberLoading: ProgressBar
    private lateinit var _btnViewMore: Button


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
        setContentView(R.layout.restaurant_detail_activity)

        _resID = intent.extras.getString(RES_ID, null)

        _uberRideButton = findViewById(R.id.ride_request_button)
        _pbRestaurantWait = findViewById(R.id.pb_restaurant_detail_fetch)
        _tvRestaurantName = findViewById(R.id.tv_restaurant_name)
        _tvRestaurantAddesss = findViewById(R.id.tv_restaurant_address)
        _tvRestaurantDistance = findViewById(R.id.tv_restaurant_distance)
        _tvRestaurantCost = findViewById(R.id.tv_restaurant_cost)
        _rbRestaurantRating = findViewById(R.id.rb_restaurant_rating)
        _ivRestaurantImage = findViewById(R.id.iv_restaurant_thumb)
        _tvRestaurantCuisines = findViewById(R.id.tv_cuisines_data)
        _llRestaurantDetails = findViewById(R.id.ll_restaurant_details)
        _tvHasOnlineDelivery = findViewById(R.id.tv_has_online_delivery)
        _tvIsDeliveringNow = findViewById(R.id.tv_is_delivering_now)
        _tvHasTableBooking = findViewById(R.id.tv_has_table_booking)
        _btnViewMore = findViewById(R.id.btn_view_more)
        _pbUberLoading = findViewById(R.id.pb_uber_loading)

        _pbUberLoading.visibility = View.INVISIBLE
        _tvRestaurantName.visibility = View.INVISIBLE
        _tvRestaurantAddesss.visibility = View.INVISIBLE
        _tvRestaurantCost.visibility = View.INVISIBLE
        _tvRestaurantDistance.visibility = View.INVISIBLE
        _rbRestaurantRating.visibility = View.INVISIBLE
        _uberRideButton.visibility = View.INVISIBLE
        _llRestaurantDetails.visibility = View.INVISIBLE
        _btnViewMore.visibility = View.INVISIBLE

        _pbRestaurantWait.isIndeterminate = true


        UberUtil.init()

        EventBus.getDefault().register(this)

        if(NetworkUtil.isNetworkAvailable(this))
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
        val restaurant = getRestaurantDetailCompletionEvent.restaurant

        Log.v(TAG, "Restaurant deatils fetch complete")
        Log.v(TAG, restaurant.toString())

        _uberRideButton.setOnClickListener({
            val rideParameters = RideParameters.Builder()
                    .setPickupLocation(GPSUtil._latitude, GPSUtil._longitude, null, null)
                    .setDropoffLocation(restaurant.location.latitude.toDouble(), restaurant.location.longitude.toDouble(), restaurant.name, restaurant.location.address)
                    .build()
            startActivity(RideRequestActivity.newIntent(this, rideParameters, UberUtil.config, null))
        })
        _tvRestaurantName.text = restaurant.name
        val sourceLocation = Location("source")
        sourceLocation.latitude = GPSUtil._latitude
        sourceLocation.longitude = GPSUtil._longitude

        val destinationLocation = Location("destination")
        destinationLocation.latitude = restaurant.location.latitude.toDouble()
        destinationLocation.longitude = restaurant.location.longitude.toDouble()

        val distance = sourceLocation.distanceTo(destinationLocation)
        _tvRestaurantDistance.text = when (distance) {
            in 0..999 -> " ${distance.toInt()} meters"
            else -> " ${(distance/1000).toInt()} Km"
        }
        _tvRestaurantCost.text = "  ${restaurant.averageCostForTwo} ${restaurant.currency}"
        _tvRestaurantAddesss.text = restaurant.location.address
        _rbRestaurantRating.rating = restaurant.userRating.aggregateRating.toFloat()
        _rbRestaurantRating.progressDrawable.setTint(Color.parseColor("#${restaurant.userRating.ratingColor}"))

        Glide.with(this).load(restaurant.featuredImage).into(_ivRestaurantImage)

        _tvRestaurantCuisines.text = restaurant.cuisines

        _pbRestaurantWait.visibility = View.GONE
        _tvRestaurantName.visibility = View.VISIBLE
        _tvRestaurantAddesss.visibility = View.VISIBLE
        _tvRestaurantCost.visibility = View.VISIBLE
        _tvRestaurantDistance.visibility = View.VISIBLE
        _rbRestaurantRating.visibility = View.VISIBLE
        _uberRideButton.visibility = View.VISIBLE
        _llRestaurantDetails.visibility = View.VISIBLE
        _btnViewMore.visibility = View.VISIBLE
        _pbUberLoading.visibility = View.VISIBLE


        when(restaurant.hasOnlineDelivery) {
            0 -> {
                _tvHasOnlineDelivery.text = " Delivers Online!!"
                _tvHasOnlineDelivery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_circle_small, 0, 0, 0)
            }
            else -> {
                _tvHasOnlineDelivery.text = " Online Delivery Unavailable"
                _tvHasOnlineDelivery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancel_circle_small, 0, 0, 0)
            }
        }

        when(restaurant.isDeliveringNow) {
            0 -> {
                _tvIsDeliveringNow.text = " Delivering Now"
                _tvIsDeliveringNow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_circle_small, 0, 0, 0)
            }
            else -> {
                _tvIsDeliveringNow.text = " Delivery Not Available Right Now"
                _tvIsDeliveringNow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancel_circle_small, 0, 0, 0)
            }
        }

        when(restaurant.hasTableBooking) {
            0 -> {
                _tvHasTableBooking.text = " Reservations Available"
                _tvHasTableBooking.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_circle_small, 0, 0, 0)
            }
            else -> {
                _tvHasTableBooking.text = " Seating Not Available"
                _tvHasTableBooking.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancel_circle_small, 0, 0, 0)
            }
        }

        _btnViewMore.setOnClickListener({
            startActivity(ZomatoWebViewActivity.newIntent(this, restaurant.url))
        })

        loadUberData(restaurant)
    }

    fun loadUberData(restaurant: Restaurant_) {

        _pbUberLoading.isIndeterminate = true
        val sessionConfiguration = SessionConfiguration.Builder()
                .setClientId(PropertyUtil.getProperty("uberClientID"))
                .setServerToken(PropertyUtil.getProperty("uberServerToken"))
                .build()

        val session = ServerTokenSession(sessionConfiguration)

        val service = UberRidesApi.with(session).build().createService()

        service.getProducts(
                GPSUtil._latitude.toFloat(),
                GPSUtil._longitude.toFloat()
        ).enqueue(object: Callback<ProductsResponse> {

            override fun onResponse(call: Call<ProductsResponse>?, response: Response<ProductsResponse>?) {
                val productList = response!!.body()

                service.getPriceEstimates(
                        GPSUtil._latitude.toFloat(),
                        GPSUtil._longitude.toFloat(),
                        restaurant.location.latitude.toFloat(),
                        restaurant.location.longitude.toFloat()
                ).enqueue(object : Callback<PriceEstimatesResponse> {

                    override fun onResponse(call: Call<PriceEstimatesResponse>?, response: Response<PriceEstimatesResponse>?) {
                        _pbUberLoading.visibility = View.GONE
                        val priceList = response!!.body()

                        var cnt = 0
                        loop@ for(price in priceList!!.prices) {
                            cnt++
                            when(cnt) {
                                1 -> findViewById<TextView>(R.id.ride1).text = "${price.displayName} : ${price.estimate}"
                                2 -> findViewById<TextView>(R.id.ride2).text = "${price.displayName} : ${price.estimate}"
                                3 -> findViewById<TextView>(R.id.ride3).text = "${price.displayName} : ${price.estimate}"
                                4 -> findViewById<TextView>(R.id.ride4).text = "${price.displayName} : ${price.estimate}"
                                else -> break@loop
                            }

                        }
                    }

                    override fun onFailure(call: Call<PriceEstimatesResponse>?, t: Throwable?) {
                    }
                })
            }

            override fun onFailure(call: Call<ProductsResponse>?, t: Throwable?) {

            }
        })
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
