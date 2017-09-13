package com.eatout.android

import android.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.*
import com.eatout.android.fragment.RestaurantListFragment
import com.eatout.android.util.GPSUtil
import com.eatout.android.util.zomato.beans.restaurant.search.SearchFilter
import com.eatout.android.util.zomato.controller.CategoriesController
import com.eatout.android.util.zomato.controller.SearchRestaurantsController
import com.eatout.android.util.zomato.events.GetCategoryCompletionEvent
import com.eatout.android.util.zomato.events.LocationUpdateEvent
import com.eatout.android.util.zomato.events.SearchRestaurantCompletionEvent
import com.google.firebase.auth.FirebaseAuth
import com.wang.avi.AVLoadingIndicatorView
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class HomeActivity : AppCompatActivity(), RestaurantListFragment.OnScrollDownToBottomListener {

    private val TAG = HomeActivity::class.java.simpleName
    private lateinit var _gpsButton: ImageButton
    private lateinit var _avGPSLoading: AVLoadingIndicatorView
    private lateinit var _avRestaurantLoading: AVLoadingIndicatorView
    private lateinit var _locationInput: EditText
    private lateinit var _categoryLoadingProgressBar: SmoothProgressBar
    private lateinit var _categoryListHorizontalScrollView: HorizontalScrollView
    private var isRestaurantSearchNeeded = true
    private var _restaurantListFragment: RestaurantListFragment? = null
    private lateinit var _filterFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Log.e(TAG, "E1")
//
//        _restaurantListFragment = RestaurantListFragment()
//        fragmentManager.beginTransaction().add(_restaurantListFragment, "RestaurantListFragment").commit()

        setContentView(R.layout.activity_home)
        Log.e(TAG, "E2")
        _gpsButton = findViewById(R.id.gps_button) as ImageButton
        _avGPSLoading = findViewById(R.id.av_gps_loading) as AVLoadingIndicatorView
        _avRestaurantLoading = findViewById(R.id.av_restaurant_loading) as AVLoadingIndicatorView

        _locationInput = findViewById(R.id.et_location_input) as EditText
        _categoryLoadingProgressBar = findViewById(R.id.pb_category_loading) as SmoothProgressBar
        _categoryListHorizontalScrollView = findViewById(R.id.hsv_category_list) as HorizontalScrollView
        _filterFab = findViewById(R.id.fab) as FloatingActionButton

        _locationInput.clearFocus()
        setupToolBar()
        fetchCategories()
        setUpGPS()


        _filterFab.setOnClickListener(View.OnClickListener { startActivity(Intent(this, FilterActivity::class.java)) })
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is RestaurantListFragment)
            _restaurantListFragment = fragment
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    private fun setUpGPS() {
        _gpsButton.setOnClickListener({
            (_gpsButton).visibility = View.INVISIBLE
            (_avGPSLoading).show()

            GPSUtil(this).fetchGPSLocation()
        })
    }

    @Subscribe
    fun onCompleteFetchGPSLocation(locationUpdateEvent: LocationUpdateEvent) {
        var location = locationUpdateEvent._loc

        val latitude = location.latitude
        val longitude = location.longitude

        Log.v(TAG, "onCompleteFetchGPSLocation() - ($latitude, $longitude) - [${locationUpdateEvent._cityName}]")

        _locationInput.setText(locationUpdateEvent._cityName)
        _locationInput.setHorizontallyScrolling(true)
        _avGPSLoading.hide()
        _gpsButton.visibility = View.VISIBLE
        _gpsButton.setBackgroundResource(R.drawable.ic_gps_fixed_black_24dp)

        if (isRestaurantSearchNeeded) {
            val searchFilter = SearchFilter()
            searchFilter.latitude = latitude
            searchFilter.longitude = longitude

            SearchRestaurantsController.searchRestaurants(searchFilter)
            _avRestaurantLoading.show()
            isRestaurantSearchNeeded = false
        }
    }

    @Subscribe
    fun onCompleteFetchRestaurantList(searchRestaurantCompletionEvent: SearchRestaurantCompletionEvent) {
        _avRestaurantLoading.hide()
        //_restaurantListFragment = RestaurantListFragment.newInstance(Gson().toJson(searchRestaurantCompletionEvent._searchResult))
        _restaurantListFragment!!.getDataFromActivity(searchRestaurantCompletionEvent._searchResult)
        Log.i(TAG, "Restaurant list fetch complete")


    }


    private fun setupToolBar() {
        val toolBar = findViewById(R.id.toolbar3) as Toolbar
        toolBar.title = ""
        toolBar.inflateMenu(R.menu.menu_main)

        toolBar.setOnMenuItemClickListener({ item ->
            if (item.itemId == R.id.action_log_out) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            false
        })
    }

    private fun fetchCategories() {
        Log.v(TAG, "In fetch Categories")
        CategoriesController.getCategories()
        (_categoryLoadingProgressBar).progressiveStart()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    fun onCompletionGetCategories(getCategoryCompletionEvent: GetCategoryCompletionEvent) {
        Log.v(TAG, getCategoryCompletionEvent._categoriesList.toString())

        var currentSelectedCategory = Button(this)

        Handler().postDelayed({
            _categoryLoadingProgressBar.progressiveStop()
            val layout = findViewById(R.id.ll_category_list) as LinearLayout

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
                        SearchRestaurantsController.searchRestaurants(searchFilter)
                        _avRestaurantLoading.show()
                    }
                })

                Log.v(TAG, "CategoryItem name is ${categoryItem._name}")
                layout.addView(buttonView)

                findViewById(R.id.ll_category_list_bg).visibility = View.VISIBLE
            }

            Handler().postDelayed({ _categoryListHorizontalScrollView.visibility = View.VISIBLE }, 100)
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
                    _avGPSLoading.hide()
                    _gpsButton.setBackgroundResource(R.drawable.ic_gps_not_fixed_black_24dp)
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
        Log.v(TAG, "Data refresh requested")
        val searchFilter = SearchFilter(startOffset = start, latitude = GPSUtil._latitude, longitude = GPSUtil._longitude)
        SearchRestaurantsController.searchRestaurants(searchFilter)
        _avRestaurantLoading.show()
    }
}
