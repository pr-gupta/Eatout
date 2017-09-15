package com.eatout.android.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.eatout.android.db.EatoutDBContract.Restaurants
import com.eatout.android.util.zomato.beans.restaurant.search.Restaurant
import com.eatout.android.util.zomato.beans.restaurant.search.SearchFilter

/**
 * Created by prashant.gup on 14/09/17.
 */
class DBRestaurantHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val TAG = javaClass.simpleName

    companion object {
        private val DATABASE_NAME = "eatout_restaurant_details.db"
        private val DATABASE_VERSION = 1

    }


    override fun onCreate(p0: SQLiteDatabase?) {
        val SQL_CREATE_RESTAURANT_TABLE = "CREATE TABLE " +
                Restaurants.TABLE_NAME + " ( " +
                Restaurants.RES_ID + " INTEGER PRIMARY KEY, " +
                Restaurants.NAME + " TEXT, " +
                Restaurants.ADDRESS + " TEXT, " +
                Restaurants.LATITUDE + " TEXT, " +
                Restaurants.LONGITUDE + " TEXT, " +
                Restaurants.AVERAGE_COST_FOR_TWO + " INT, " +
                Restaurants.CURRENCY + " TEXT, " +
                Restaurants.CUISINES + " TEXT, " +
                Restaurants.THUMB_URL + " TEXT, " +
                Restaurants.FEATURED_IMAGE + " TEXT, " +
                Restaurants.USER_RATING + " REAL, " +
                Restaurants.RATING_COLOR + " TEXT, " +
                Restaurants.DOES_DELIVERS_ONLINE + " INT, " +
                Restaurants.IS_DELIVERING_NOW + " INT, " +
                Restaurants.IS_RESERVATION_AVAILABLE + " INT " +
                ");"
        p0!!.execSQL(SQL_CREATE_RESTAURANT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS " + Restaurants.TABLE_NAME)
        onCreate(p0)
    }

    fun insertRestaurant(restaurant_: Restaurant):Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        val restaurant = restaurant_.restaurant

        values.put(Restaurants.RES_ID, restaurant.r.resId)
        values.put(Restaurants.NAME, restaurant.name)
        values.put(Restaurants.ADDRESS, restaurant.location.address)
        values.put(Restaurants.LATITUDE, restaurant.location.latitude)
        values.put(Restaurants.LONGITUDE, restaurant.location.longitude)
        values.put(Restaurants.AVERAGE_COST_FOR_TWO, restaurant.averageCostForTwo)
        values.put(Restaurants.CURRENCY, restaurant.currency)
        values.put(Restaurants.CUISINES, restaurant.cuisines)
        values.put(Restaurants.THUMB_URL, restaurant.thumb)
        values.put(Restaurants.FEATURED_IMAGE, restaurant.featuredImage)
        values.put(Restaurants.USER_RATING, restaurant.userRating.aggregateRating)
        values.put(Restaurants.RATING_COLOR, restaurant.userRating.ratingColor)
        values.put(Restaurants.DOES_DELIVERS_ONLINE, restaurant.hasOnlineDelivery)
        values.put(Restaurants.IS_DELIVERING_NOW, restaurant.isDeliveringNow)
        values.put(Restaurants.IS_RESERVATION_AVAILABLE, restaurant.hasTableBooking)

        var isInsertSuccessful = false
        try {
            isInsertSuccessful = db.insertOrThrow(Restaurants.TABLE_NAME, null, values) > 0
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
                db.close()
        }
        return isInsertSuccessful
    }

    fun getRestaurant(resID: String):Restaurant {

        Log.v(TAG, resID)

        val db = this.readableDatabase

        val cursor = db.query(Restaurants.TABLE_NAME, null, "${Restaurants.RES_ID} = $resID", null, null, null, null)
        var restaurant = Restaurant()

        if(cursor.isBeforeFirst) {
            cursor.moveToNext()
            restaurant = getRestaurantFromCursor(cursor)
        } else {
            Log.w(TAG, "Found empty cursor! This should not happen as the complete restaurant details is stored at the same time")
        }

        cursor.close()
        db.close()
        return restaurant
    }

    fun getRestaurantList(searchFilter: SearchFilter):MutableList<Restaurant> {
        val restaurants: MutableList<Restaurant> = mutableListOf()

        val db = this.readableDatabase
        val cursor = db.query(Restaurants.TABLE_NAME, null, null, null, null, null, null)

        while(cursor.moveToNext()) {
            restaurants.add(getRestaurantFromCursor(cursor))
        }

        cursor.close()
        db.close()

        return restaurants
    }

    fun getRestaurantFromCursor(cursor: Cursor):Restaurant {
        val restaurant = Restaurant()
        restaurant.restaurant.r.resId = cursor.getInt(cursor.getColumnIndex(Restaurants.RES_ID))
        restaurant.restaurant.id = cursor.getString(cursor.getColumnIndex(Restaurants.RES_ID))
        restaurant.restaurant.name = cursor.getString(cursor.getColumnIndex(Restaurants.NAME))
        restaurant.restaurant.location.address = cursor.getString(cursor.getColumnIndex(Restaurants.ADDRESS))
        restaurant.restaurant.location.latitude = cursor.getString(cursor.getColumnIndex(Restaurants.LATITUDE))
        restaurant.restaurant.location.longitude = cursor.getString(cursor.getColumnIndex(Restaurants.LONGITUDE))
        restaurant.restaurant.averageCostForTwo = cursor.getInt(cursor.getColumnIndex(Restaurants.AVERAGE_COST_FOR_TWO))
        restaurant.restaurant.currency = cursor.getString(cursor.getColumnIndex(Restaurants.CURRENCY))
        restaurant.restaurant.thumb = cursor.getString(cursor.getColumnIndex(Restaurants.THUMB_URL))
        restaurant.restaurant.featuredImage = cursor.getString(cursor.getColumnIndex(Restaurants.FEATURED_IMAGE))
        restaurant.restaurant.userRating.aggregateRating = cursor.getString(cursor.getColumnIndex(Restaurants.USER_RATING))
        restaurant.restaurant.userRating.ratingColor = cursor.getString(cursor.getColumnIndex(Restaurants.RATING_COLOR))
        restaurant.restaurant.hasOnlineDelivery = cursor.getInt(cursor.getColumnIndex(Restaurants.DOES_DELIVERS_ONLINE))
        restaurant.restaurant.isDeliveringNow = cursor.getInt(cursor.getColumnIndex(Restaurants.IS_DELIVERING_NOW))
        restaurant.restaurant.hasTableBooking = cursor.getInt(cursor.getColumnIndex(Restaurants.IS_RESERVATION_AVAILABLE))

        return restaurant
    }
}