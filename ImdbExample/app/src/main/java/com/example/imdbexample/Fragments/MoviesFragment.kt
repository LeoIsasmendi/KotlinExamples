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
import com.example.imdbexample.Models.ViewHolders.MovieViewHolder
import com.example.imdbexample.Services.Helper
import com.example.imdbexample.Services.IMDBService
import com.example.imdbexample.Services.ServiceFactory
import kotlinx.android.synthetic.main.fragment_movies_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviesFragment : Fragment() {

    private val maxPageNumber = 100
    private var pageNumber = 1
    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var mAdapter: GenericRecyclerViewAdapter<Movie>

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_page_counter.text = getPageNumberText()
        btn_next.setOnClickListener {
            pageNumber = if (pageNumber < maxPageNumber) pageNumber + 1 else pageNumber
            text_page_counter.text = getPageNumberText()
            fetchMovies()
        }
        btn_prev.setOnClickListener {
            pageNumber = if (pageNumber > 1) pageNumber - 1 else 1
            text_page_counter.text = getPageNumberText()
            fetchMovies()
        }
        initRecycleView()
        fetchMovies()
    }

    private fun initRecycleView() {
        mAdapter = object : GenericRecyclerViewAdapter<Movie>(listener) {

            override fun getLayoutId(position: Int, obj: Movie): Int {
                return R.layout.fragment_movies
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return MovieViewHolder(view)
            }

        }

        list.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        list.adapter = mAdapter

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
                        mAdapter.setItems(response.body()!!.results)
                    }

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

}
