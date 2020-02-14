package com.example.imdbexample.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbexample.LocalStorage.LocalStorage
import com.example.imdbexample.Models.MovieDetailsResponse
import com.example.imdbexample.Models.ViewHolders.MovieDetailsViewHolder

import com.example.imdbexample.R
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    private var columnCount = 2
    private lateinit var mAdapter: GenericRecyclerViewAdapter<MovieDetailsResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
    }

    private fun initRecycleView() {
        mAdapter = object : GenericRecyclerViewAdapter<MovieDetailsResponse>(null) {

            override fun getLayoutId(position: Int, obj: MovieDetailsResponse): Int {
                return R.layout.fragment_movies
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return MovieDetailsViewHolder(view)
            }

        }

        list.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        list.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        fetchFavorites()
    }

    private fun fetchFavorites() {
        val localStorage = LocalStorage(activity!!.applicationContext)
        val result = localStorage.getMovies()

        if (result.isEmpty()) {
            empty_list.visibility = View.VISIBLE
        } else {
            empty_list.visibility = View.GONE
            mAdapter.setItems(result)
        }

    }

}
