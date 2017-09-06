package com.eatout.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.preference.*
import android.util.Log
import android.view.View
import android.widget.Button
import com.eatout.android.util.zomato.beans.common.categories.Category
import com.eatout.android.util.zomato.controller.CategoriesController
import com.eatout.android.util.zomato.events.GetCategoryCompletionEvent
import com.pavelsikun.seekbarpreference.SeekBarPreference
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import com.eatout.android.util.CustomMultiSelectListPreference
import com.eatout.android.util.GPSUtil
import com.eatout.android.util.zomato.beans.restaurant.search.OrderBy
import com.eatout.android.util.zomato.beans.restaurant.search.SearchFilter
import com.eatout.android.util.zomato.beans.restaurant.search.SortParam
import com.eatout.android.util.zomato.controller.SearchRestaurantsController


class FilterActivity : AppCompatActivity() {

    private lateinit var _filterButton: Button
    private val TAG = FilterActivity::class.java.simpleName

    companion object {
        val EDIT_TEXT_KEYWORD_KEY = "et_keyword_preference"
        val DISTANCE_SEEK_BAR_KEY = "distance_seekbar"
        val CATEGORY_LIST_KEY = "category_list"
        val SORT_BY_LIST_KEY = "sort_by_list"
        val ORDER_BY_LIST_KEY = "order_by_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_filter)

        _filterButton = findViewById(R.id.btn_filter) as Button

        setupFilterButton()
        setupPreference()
    }

    private fun setupPreference() {

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(EDIT_TEXT_KEYWORD_KEY, "").apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(DISTANCE_SEEK_BAR_KEY, 5000).apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit().putStringSet(CATEGORY_LIST_KEY, setOf("2")).apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(SORT_BY_LIST_KEY, "").apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(ORDER_BY_LIST_KEY, "").apply()

    }

    private fun setupFilterButton() {
        _filterButton.setOnClickListener(View.OnClickListener {
            val searchFilter = SearchFilter()

            searchFilter.query = PreferenceManager.getDefaultSharedPreferences(this).getString(EDIT_TEXT_KEYWORD_KEY, null)
            searchFilter.category = PreferenceManager.getDefaultSharedPreferences(this)
                    .getStringSet(CATEGORY_LIST_KEY, null)
                    .map { it.toInt() }
                    .toTypedArray()
            searchFilter.radius = PreferenceManager.getDefaultSharedPreferences(this).getInt(DISTANCE_SEEK_BAR_KEY, 5000).toDouble()
            searchFilter.sort = when (PreferenceManager.getDefaultSharedPreferences(this).getString(SORT_BY_LIST_KEY, "")) {
                "" -> SortParam.NONE
                "cost" -> SortParam.COST
                "rating" -> SortParam.RATING
                "real_distance" -> SortParam.REAL_DISTANCE
                else -> SortParam.NONE
            }

            searchFilter.orderBy = when(PreferenceManager.getDefaultSharedPreferences(this).getString(ORDER_BY_LIST_KEY, "")) {
                "" -> OrderBy.NONE
                "asc" -> OrderBy.ASC
                "desc" -> OrderBy.DESC
                else -> OrderBy.NONE
            }

            searchFilter.latitude = GPSUtil._latitude
            searchFilter.longitude = GPSUtil._longitude

            Log.v(TAG, searchFilter.toString())
            SearchRestaurantsController.searchRestaurants(searchFilter)
            finish()
        } )
    }

    class FilterFragment: PreferenceFragment() {

        private val TAG = FilterFragment::class.java.simpleName


        private lateinit var _editTextKeywordPreference: EditTextPreference
        private lateinit var _distanceSeekbarPreference: SeekBarPreference
        private lateinit var _categoryListPreference: CustomMultiSelectListPreference
        private lateinit var _sortByListPreference: ListPreference
        private lateinit var _orderByListPreference: ListPreference

        private var _category: List<Category> = listOf()


        override fun onStart() {
            super.onStart()
            EventBus.getDefault().register(this)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.filter_pref)

            _editTextKeywordPreference = findPreference(EDIT_TEXT_KEYWORD_KEY) as EditTextPreference
            _distanceSeekbarPreference = findPreference(DISTANCE_SEEK_BAR_KEY) as SeekBarPreference
            _categoryListPreference = findPreference(CATEGORY_LIST_KEY) as CustomMultiSelectListPreference
            _sortByListPreference = findPreference(SORT_BY_LIST_KEY) as ListPreference
            _orderByListPreference = findPreference(ORDER_BY_LIST_KEY) as ListPreference

            setUpEditTextKeywordPreference()
            setUpCategoryListPreference()
            setUpSortByListPreference()
            setUpOrderByListPreference()
        }

        override fun onAttach(context: Context?) {
            super.onAttach(context)
        }

        private fun setUpEditTextKeywordPreference() {
            _editTextKeywordPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, o ->
                val yourString = o.toString()
                _editTextKeywordPreference.summary = yourString
                true
            }
        }

        private fun setUpCategoryListPreference() {
            _categoryListPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                if(_category.isEmpty()) {
                    _categoryListPreference.dialog.dismiss()
                    CategoriesController.getCategories()
                }
                true
            }

            _categoryListPreference.setOnPreferenceChangeListener { _, o ->

                if(o is java.util.HashSet<*>)
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putStringSet(CATEGORY_LIST_KEY, (o as HashSet<String>)).apply()
                updateCategoryListPreferenceSummary()
                true
            }
            
        }

        private fun updateCategoryListPreferenceSummary() {
            val st =  PreferenceManager.getDefaultSharedPreferences(activity).getStringSet(CATEGORY_LIST_KEY, null)

            val tArray = MutableList(st.size, {""})
            var cnt = 0

            for(i in st) {
                tArray[cnt++] = _category[i.toInt() - 1].categoryItem._name
            }
            _categoryListPreference.summary = tArray.joinToString()
        }

        @Subscribe
        fun onCompletionGetCategories(getCategoryCompletionEvent: GetCategoryCompletionEvent) {

            _category = getCategoryCompletionEvent._categoriesList._categories
            val entries = Array<CharSequence>(_category.size, {""})
            val entryValues = Array<CharSequence>(_category.size, {""})
            for(i in _category.indices) {
                entries[i] = _category[i].categoryItem._name
                entryValues[i] = _category[i].categoryItem._id.toString()
            }
            _categoryListPreference.entries = entries
            _categoryListPreference.entryValues = entryValues
            updateCategoryListPreferenceSummary()
            _categoryListPreference.customClick()

        }

        private fun setUpSortByListPreference() {
            _sortByListPreference.setOnPreferenceChangeListener { preference, any ->
                Log.v(TAG, "In sort - $any")
                preference.editor.putString(SORT_BY_LIST_KEY, any.toString()).commit()
                _sortByListPreference.summary = any.toString()
                true
            }
        }

        private fun setUpOrderByListPreference() {
            _orderByListPreference.setOnPreferenceChangeListener { preference, any ->
                Log.v(TAG, any.toString())
                preference.editor.putString(ORDER_BY_LIST_KEY, any.toString()).commit()
                _orderByListPreference.summary = any.toString()
                true
            }
        }


        override fun onStop() {
            EventBus.getDefault().unregister(this)
            super.onStop()
        }
    }
}
