package com.eatout.android.util.zomato.beans

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView

import com.eatout.android.R

class ZomatoWebViewActivity : Activity() {

    private lateinit var _wvRestaurantDetails: WebView

    companion object {
        private val RESTAURANT_URL = "restaurant_url"

        fun newIntent(context: Context, url: String): Intent {
            val intent = Intent(context, ZomatoWebViewActivity::class.java)

            intent.putExtra(RESTAURANT_URL, url)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zomato_web_view)

        _wvRestaurantDetails = findViewById(R.id.wv_restaurant_detail)

        _wvRestaurantDetails.settings.javaScriptEnabled = true
        _wvRestaurantDetails.loadUrl(intent.extras.getString(RESTAURANT_URL))
    }
}
