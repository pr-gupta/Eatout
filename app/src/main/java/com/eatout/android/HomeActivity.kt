package com.eatout.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.eatout.android.util.zomato.common.CategoriesController
import com.eatout.android.util.zomato.events.GetCategoryCompletionEvent
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import android.util.TypedValue




class HomeActivity : AppCompatActivity() {

    private val TAG = HomeActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home)

        setupToolBar()
        fetchCategories()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    private fun setupToolBar() {
        val toolBar = findViewById(R.id.toolbar3) as Toolbar
        toolBar.title = "Toolbar"
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
        (findViewById(R.id.pb_category_loading) as SmoothProgressBar).progressiveStart()
    }

    @Subscribe
    fun onCompletionGetCategories(getCategoryCompletionEvent: GetCategoryCompletionEvent) {
        Log.v(TAG, getCategoryCompletionEvent._categoriesList.toString())



        Handler().postDelayed({
            (findViewById(R.id.pb_category_loading) as SmoothProgressBar).progressiveStop()
            val layout = findViewById(R.id.ll_category_list) as LinearLayout

            for((categoryItem) in getCategoryCompletionEvent._categoriesList._categories) {
                val buttonView = Button(this)
                buttonView.text = categoryItem._name

                var px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, resources.displayMetrics)
                var ipx = px.toInt()

                buttonView.setPadding(ipx, ipx, ipx, ipx)
                Log.v(TAG, "CategoryItem name is ${categoryItem._name}")
                layout.addView(buttonView)
            }

            Handler().postDelayed({findViewById(R.id.hsv_category_list).visibility = View.VISIBLE}, 100)
        }, 500)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
