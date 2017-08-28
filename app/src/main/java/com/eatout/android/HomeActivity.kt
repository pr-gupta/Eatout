package com.eatout.android

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import com.eatout.android.util.GPSUtil
import com.eatout.android.util.zomato.common.CategoriesController
import com.eatout.android.util.zomato.events.GetCategoryCompletionEvent
import com.eatout.android.util.zomato.events.LocationUpdateEvent
import com.wang.avi.AVLoadingIndicatorView
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import android.location.Geocoder
import android.text.Editable
import android.widget.*


class HomeActivity : AppCompatActivity() {

    private val TAG = HomeActivity::class.java.simpleName
    private lateinit var _gpsButton: ImageButton
    private lateinit var _avGPSLoading: AVLoadingIndicatorView
    private lateinit var _locationInput: EditText
    private lateinit var _categoryLoadingProgressBar: SmoothProgressBar
    private lateinit var _categoryListHorizontalScrollView: HorizontalScrollView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home)

        _gpsButton = findViewById(R.id.gps_button) as ImageButton
        _avGPSLoading = findViewById(R.id.av_gps_loading) as AVLoadingIndicatorView
        _locationInput = findViewById(R.id.et_location_input) as EditText
        _categoryLoadingProgressBar = findViewById(R.id.pb_category_loading) as SmoothProgressBar
        _categoryListHorizontalScrollView = findViewById(R.id.hsv_category_list) as HorizontalScrollView

        setupToolBar()
        fetchCategories()
        setUpGPS()
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

        Log.v(TAG, "onCompleteFetchGPSLocation() - ($latitude, $latitude) - [${locationUpdateEvent._cityName}]")

        _locationInput.setText(locationUpdateEvent._cityName)
        _locationInput.setHorizontallyScrolling(true)
        _avGPSLoading.hide()
        _gpsButton.visibility = View.VISIBLE
        _gpsButton.setBackgroundResource(R.drawable.ic_gps_fixed_black_24dp)
    }

    private fun setupToolBar() {
        val toolBar = findViewById(R.id.toolbar3) as Toolbar
        toolBar.title = ""
        toolBar.inflateMenu(R.menu.menu_main)

        toolBar.setOnMenuItemClickListener({ item ->
            if (item.itemId == R.id.item1) {

            }
            false
        })
    }

    private fun fetchCategories() {
        Log.v(TAG, "In fetch Categories")
        CategoriesController.getCategories()
        (_categoryLoadingProgressBar).progressiveStart()
    }

    @Subscribe
    fun onCompletionGetCategories(getCategoryCompletionEvent: GetCategoryCompletionEvent) {
        Log.v(TAG, getCategoryCompletionEvent._categoriesList.toString())



        Handler().postDelayed({
            _categoryLoadingProgressBar.progressiveStop()
            val layout = findViewById(R.id.ll_category_list) as LinearLayout

            for((categoryItem) in getCategoryCompletionEvent._categoriesList._categories) {
                val buttonView = Button(this)
                buttonView.text = categoryItem._name + " - " + categoryItem._id

                var px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, resources.displayMetrics)
                var ipx = px.toInt()

                buttonView.setPadding(ipx, ipx, ipx, ipx)
                Log.v(TAG, "CategoryItem name is ${categoryItem._name}")
                layout.addView(buttonView)
            }

            Handler().postDelayed({_categoryListHorizontalScrollView.visibility = View.VISIBLE}, 100)
        }, 500)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpGPS()
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
}
