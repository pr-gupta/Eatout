package com.eatout.android.fragment

import android.app.Fragment
import android.content.Context
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eatout.android.adapter.RestaurantListAdaptor
import com.eatout.android.databinding.FragmentRestaurantListBinding
import com.eatout.android.model.view.RecyclerViewModel
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RestaurantListFragment.OnScrollDownToBottomListener] interface
 * to handle interaction events.
 */
class RestaurantListFragment : Fragment() {

    private var _searchResult: SearchResult = SearchResult()
    private lateinit var mListener: OnScrollDownToBottomListener
    private var loading = true
    private lateinit var _binding: FragmentRestaurantListBinding

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentRestaurantListBinding.inflate(inflater, container, false).apply {
            viewModel = RecyclerViewModel(
                    ObservableField(RestaurantListAdaptor(activity, _searchResult, { start -> mListener.refreshData(start) })),
                    ObservableField(GridLayoutManager(activity, 2)))
        }

        return _binding.root
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
        _binding.viewModel.adapter.get().notifyDataSetChanged()

        if(scrollUpRequired)
            _binding.viewModel.scrollToTop()

        loading = true
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     */
    interface OnScrollDownToBottomListener {
        fun refreshData(start: Int)
    }
}
