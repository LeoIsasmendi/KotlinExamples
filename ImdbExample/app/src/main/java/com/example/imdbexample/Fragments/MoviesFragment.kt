package com.example.imdbexample.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imdbexample.R

import com.example.imdbexample.Models.Movie
import com.example.imdbexample.Models.MovieResponse
import com.example.imdbexample.Services.Helper
import com.example.imdbexample.Services.IMDBService
import com.example.imdbexample.Services.ServiceFactory
import kotlinx.android.synthetic.main.fragment_movies_list.*
import kotlinx.android.synthetic.main.fragment_movies_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviesFragment : Fragment() {

    private val maxPageNumber = 100
    private var pageNumber = 1
    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        view.text_page_counter.text = getPageNumberText()

        // Set the adapter
        if (view.list is RecyclerView) {
            view.list.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            fetchMovies()
        }

        view.btn_next.setOnClickListener {
            pageNumber = if (pageNumber < maxPageNumber) pageNumber + 1 else pageNumber
            view.text_page_counter.text = getPageNumberText()
            fetchMovies()
        }
        view.btn_prev.setOnClickListener {
            pageNumber = if (pageNumber > 1) pageNumber - 1 else 1
            view.text_page_counter.text = getPageNumberText()
            fetchMovies()
        }

        return view
    }

    private fun getPageNumberText(): String {
        return getString(R.string.page_counter) + pageNumber.toString()
    }

    private fun fetchMovies() {
        val mService: IMDBService = ServiceFactory.IMDB.create(Helper.API_KEY)
        val call = mService.getDiscoverMovies(pageNumber)

        progress_circular?.visibility = View.VISIBLE

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {

                progress_circular.visibility = View.GONE

                if (response.code() == 200) {
                    if (response.body()!!.results.isEmpty()) {
                        empty_list?.visibility = View.VISIBLE
                    } else {
                        empty_list?.visibility = View.GONE
                        list.adapter =
                            MyMoviesRecyclerViewAdapter(response.body()!!.results, listener)
                    }

                    Log.d(MoviesFragment.toString(), response.body()!!.results.toString())
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                progress_circular?.visibility = View.GONE
                Log.e(MoviesFragment.toString(), t.toString())
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Movie?)
    }

}
