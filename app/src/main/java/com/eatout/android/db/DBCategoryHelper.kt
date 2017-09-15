package com.eatout.android.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.eatout.android.db.EatoutDBContract.Categories
import com.eatout.android.util.zomato.beans.common.categories.CategoriesList
import com.eatout.android.util.zomato.beans.common.categories.Category
import com.eatout.android.util.zomato.beans.common.categories.CategoryItem

/**
 * Created by prashant.gup on 15/09/17.
 */
class DBCategoryHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val TAG = javaClass.simpleName

    companion object {
        private val DATABASE_NAME = "eatout_category_details.db"
        private val DATABASE_VERSION = 1

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " +
                Categories.TABLE_NAME + " ( " +
                Categories.CATEGORY_ID + " INTEGER PRIMARY KEY, " +
                Categories.CATEGORY_NAME + " TEXT " +
                ");"
        p0!!.execSQL(SQL_CREATE_CATEGORY_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS " + Categories.TABLE_NAME)
        onCreate(p0)
    }

    fun insertCategory(category: Category):Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(Categories.CATEGORY_ID, category.categoryItem._id)
        values.put(Categories.CATEGORY_NAME, category.categoryItem._name)

        var isInsertSuccessful = false
        try {
            isInsertSuccessful = db.insertOrThrow(Categories.TABLE_NAME, null, values) > 0
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return isInsertSuccessful
    }

    fun getCategories():CategoriesList {
        val categoriesList = CategoriesList()

        val db = this.readableDatabase
        val cursor = db.query(Categories.TABLE_NAME, null, null, null, null, null, null)

        while(cursor.moveToNext()) {
            categoriesList._categories.add(getCategoryFromCursor(cursor))
        }

        cursor.close()
        db.close()

        return categoriesList
    }

    fun getCategory(id: Int):Category {
        Log.v(TAG, id.toString())

        val db = this.readableDatabase

        val cursor = db.query(Categories.TABLE_NAME, null, "${Categories.CATEGORY_ID} = $id", null, null, null, null)
        var category = Category()

        if(cursor.isBeforeFirst) {
            cursor.moveToNext()
            category = getCategoryFromCursor(cursor)
        } else {
            Log.w(TAG, "Found empty cursor! This should not happen as the complete Category details is stored at the same time")
        }

        cursor.close()
        db.close()
        return category
    }

    private fun getCategoryFromCursor(cursor: Cursor):Category {
        return Category(CategoryItem(
                cursor.getInt(cursor.getColumnIndex(Categories.CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndex(Categories.CATEGORY_NAME))
        ))
    }
}