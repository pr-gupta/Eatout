package com.eatout.android

import android.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import com.eatout.android.databinding.ActivityHomeBinding
import com.eatout.android.db.DBRestaurantHelper
import com.eatout.android.fragment.RestaurantListFragment
import com.eatout.android.model.view.HomeActivityViewModel
import com.eatout.android.util.GPSUtil
import com.eatout.android.util.NetworkUtil
import com.eatout.android.util.zomato.beans.restaurant.search.SearchFilter
import com.eatout.android.util.zomato.controller.*
import com.eatout.android.util.zomato.events.GetCategoryCompletionEvent
import com.eatout.android.util.zomato.events.LocationUpdateEvent
import com.eatout.android.util.zomato.events.SearchRestaurantCompletionEvent
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class HomeActivity : AppCompatActivity(), RestaurantListFragment.OnScrollDownToBottomListener, HomeActivityViewModel.HomeActivityViewModelChangedListener {

    private val TAG = HomeActivity::class.java.simpleName
    private var isRestaurantSearchNeeded = true
    private var _restaurantListFragment: RestaurantListFragment? = null
    private lateinit var _searchRestaurantController : ISearchRestaurant
    private lateinit var _binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        _binding.viewModel = HomeActivityViewModel(context = this)

        _searchRestaurantController = if(NetworkUtil.isNetworkAvailable(this))
            SearchRestaurantsController(this)
        else
            SearchRestaurantControllerOffline(this)

        _binding.toolbar3.title = ""
        setSupportActionBar(_binding.toolbar3)
        fetchCategories()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is RestaurantListFragment)
            _restaurantListFragment = fragment
    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
    }

    @Subscribe
    fun onCompleteFetchGPSLocation(locationUpdateEvent: LocationUpdateEvent) {
        var location = locationUpdateEvent._loc

        val latitude = location.latitude
        val longitude = location.longitude

        Log.v(TAG, "onCompleteFetchGPSLocation() - ($latitude, $longitude) - [${locationUpdateEvent._cityName}]")

        _binding.viewModel.locationInput.set(locationUpdateEvent._cityName)
        _binding.viewModel.gpsAVLoadingIndicatorVisibility.set(false)
        _binding.viewModel.gpsLocationFound.set(true)

        if (isRestaurantSearchNeeded) {
            val searchFilter = SearchFilter()
            searchFilter.latitude = latitude
            searchFilter.longitude = longitude

            _searchRestaurantController.searchRestaurants(searchFilter)
            _binding.avRestaurantLoading.show()
            isRestaurantSearchNeeded = false
        }
    }

    @Subscribe
    fun onCompleteFetchRestaurantList(searchRestaurantCompletionEvent: SearchRestaurantCompletionEvent) {
        _binding.avRestaurantLoading.hide()

        if(NetworkUtil.isNetworkAvailable(this)) {
            val db = DBRestaurantHelper(this)

            for (restaurant in searchRestaurantCompletionEvent._searchResult.restaurants) {
                Log.v(TAG, "Insertion in db was successful = ${db.insertRestaurant(restaurant)}")
            }
        }

        _restaurantListFragment!!.getDataFromActivity(searchRestaurantCompletionEvent._searchResult)
        Log.i(TAG, "Restaurant list fetch complete")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                R.id.action_log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        return false
    }

    private fun fetchCategories() {
        Log.v(TAG, "In fetch Categories")

        if(NetworkUtil.isNetworkAvailable(this))
            CategoriesController(this).getCategories()
        else
            CategoriesControllerOffline(this).getCategories()

        _binding.pbCategoryLoading.progressiveStart()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    fun onCompletionGetCategories(getCategoryCompletionEvent: GetCategoryCompletionEvent) {
        Log.v(TAG, getCategoryCompletionEvent._categoriesList.toString())

        var currentSelectedCategory = Button(this)

        Handler().postDelayed({
            _binding.pbCategoryLoading.progressiveStop()
            val layout = _binding.llCategoryList

            for ((categoryItem) in getCategoryCompletionEvent._categoriesList._categories) {
                val buttonView = Button(this)
                buttonView.text = categoryItem._name

                val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, resources.displayMetrics)
                val ipx = px.toInt()

                buttonView.setPadding(ipx, ipx, ipx, ipx)
                buttonView.setBackgroundColor(Color.TRANSPARENT)

                if (categoryItem._id == 2) {
                    buttonView.setTextColor(Color.parseColor("#64dd17"))
                    buttonView.background = getDrawable(R.drawable.bottom_shadow)
                    currentSelectedCategory = buttonView
                }

                val finalCategoryItem = categoryItem
                buttonView.setOnClickListener({ view ->

                    if ((view as Button).text.toString() != currentSelectedCategory.text.toString()) {
                        view.setTextColor(Color.parseColor("#64dd17"))
                        view.background = getDrawable(R.drawable.bottom_shadow)

                        currentSelectedCategory.setTextColor(Color.BLACK)
                        currentSelectedCategory.background = getDrawable(android.R.drawable.btn_default)
                        currentSelectedCategory.setBackgroundColor(Color.TRANSPARENT)
                        currentSelectedCategory.setPadding(ipx, ipx, ipx, ipx)
                        currentSelectedCategory = view
                    }
                    if (GPSUtil._latitude != 0.0) {
                        val searchFilter = SearchFilter(latitude = GPSUtil._latitude, longitude = GPSUtil._longitude, category = arrayOf(finalCategoryItem._id))
                        Log.v(TAG, searchFilter.toString())
                        _searchRestaurantController.searchRestaurants(searchFilter)
                        _binding.avRestaurantLoading.show()
                    }
                })

                Log.v(TAG, "CategoryItem name is ${categoryItem._name}")
                layout.addView(buttonView)

                _binding.llCategoryListBg.visibility = View.VISIBLE
            }

            Handler().postDelayed({ _binding.hsvCategoryList.visibility = View.VISIBLE }, 100)
        }, 500)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "User gave permission to get location by GPS")
                    GPSUtil(this).fetchGPSLocation()
                } else {
                    Log.w(TAG, "User didn't gave permission to get location by GPS")
                    _binding.viewModel.gpsAVLoadingIndicatorVisibility.set(false)
                }
                return
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun refreshData(start: Int) {
        if(!NetworkUtil.isNetworkAvailable(this))
            return
        Log.v(TAG, "Data refresh requested")
        val searchFilter = SearchFilter(startOffset = start, latitude = GPSUtil._latitude, longitude = GPSUtil._longitude)
        _searchRestaurantController.searchRestaurants(searchFilter)
        _binding.avRestaurantLoading.show()
    }

    override fun turnOffRestaurantSearchNeeded() {
        isRestaurantSearchNeeded = false
    }

    override fun turnOnRestaurantSearchNeeded() {
        isRestaurantSearchNeeded = true
    }
}
