package com.eatout.android.fragment

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eatout.android.R
import com.eatout.android.adapter.RestaurantListAdaptor
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RestaurantListFragment.OnScrollDownToBottomListener] interface
 * to handle interaction events.
 */
class RestaurantListFragment : Fragment() {

    private lateinit var _recyclerView: RecyclerView
    private lateinit var _restaurantListAdapter: RestaurantListAdaptor
    private var _searchResult: SearchResult = SearchResult()
    private var mListener: OnScrollDownToBottomListener? = null
    private var loading = true

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val rootView = inflater!!.inflate(R.layout.fragment_restaurant_list, container, false)

        Log.v(TAG, "Inside on CreateView")
        Log.v(TAG, _searchResult.toString())
        Log.v(TAG, _searchResult.restaurants.size.toString())

        _recyclerView = rootView.findViewById(R.id.restaurant_list)
        _restaurantListAdapter = RestaurantListAdaptor(activity, _searchResult)
        _recyclerView.adapter = _restaurantListAdapter
        _recyclerView.layoutManager = GridLayoutManager(activity, 2)

        _recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            var ydy = 0
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) //check for scroll down
                {
                    val mLayoutManager = _recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = mLayoutManager.childCount
                    val totalItemCount = mLayoutManager.itemCount
                    val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisibleItems) >= totalItemCount)
                        {
                            loading = false
                            Log.v("...", "Last Item Wow !");
                            mListener!!.refreshData(_searchResult.resultsStart + _searchResult.resultsShown)
                        }
                    }
                }
            }
        })


        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnScrollDownToBottomListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    fun getDataFromActivity(searchResult: SearchResult) {

        if(searchResult.resultsShown == 0)
            return

        var scrollUpRequired = false
        if(searchResult.resultsStart < _searchResult.resultsStart + _searchResult.resultsShown) {
            _searchResult.restaurants.clear()
            scrollUpRequired = true
        }
        _searchResult.restaurants.addAll(searchResult.restaurants)


        _searchResult.resultsFound = searchResult.resultsFound
        _searchResult.resultsShown = searchResult.resultsShown
        _searchResult.resultsStart = searchResult.resultsStart
        Log.d(TAG, "Inside getDataFromActivity" + _searchResult.toString())
        _restaurantListAdapter.notifyDataSetChanged()

        if(scrollUpRequired)
            (_recyclerView.layoutManager as GridLayoutManager).scrollToPositionWithOffset(0, 0)
        loading = true
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnScrollDownToBottomListener {
        fun refreshData(start: Int)
    }


}
