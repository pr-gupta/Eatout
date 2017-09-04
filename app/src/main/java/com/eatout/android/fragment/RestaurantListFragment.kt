package com.eatout.android.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.eatout.android.R
import com.eatout.android.adapter.RestaurantListAdaptor
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult
import com.google.gson.Gson
import java.io.FileReader
import java.io.InputStreamReader

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RestaurantListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RestaurantListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantListFragment : Fragment() {

    lateinit var _recyclerView: RecyclerView
    lateinit var _restaurantListAdapter: RestaurantListAdaptor
    var _searchResult: SearchResult = SearchResult()
    private val TAG = RestaurantListFragment::class.java.simpleName
    private var mListener: OnFragmentInteractionListener? = null

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
//        _recyclerView.itemAnimator = DefaultItemAnimator()
        return rootView
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    fun getDataFromActivity(searchResult: SearchResult) {
        _searchResult.resultsFound = searchResult.resultsFound
        _searchResult.resultsShown = searchResult.resultsShown
        _searchResult.resultsStart = searchResult.resultsStart
        _searchResult.restaurants.clear()
        _searchResult.restaurants.addAll(searchResult.restaurants)
        Log.d(TAG, "Inside getDataFromActivity" + _searchResult.toString())
        _restaurantListAdapter.notifyDataSetChanged()
//
//  _recyclerView.adapter = RestaurantListAdaptor(activity, searchResult)
//        _recyclerView.invalidate()

        // _recyclerView.swapAdapter(RestaurantListAdaptor(activity, searchResult.restaurants), true)
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
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


}
