package com.example.imdbexample.Fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbexample.Models.Movie
import com.example.imdbexample.Models.MovieResponse
import com.example.imdbexample.Models.ViewHolders.MovieViewHolder

import com.example.imdbexample.R
import com.example.imdbexample.Services.Helper
import com.example.imdbexample.Services.IMDBService
import com.example.imdbexample.Services.ServiceFactory
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.empty_list
import kotlinx.android.synthetic.main.fragment_search.list
import kotlinx.android.synthetic.main.fragment_search.progress_circular
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var columnCount = 2
    private val mService: IMDBService = ServiceFactory.IMDB.create()
    private var mQuery: String = ""

    private lateinit var mAdapter: GenericRecyclerViewAdapter<Movie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                mQuery = s.toString()
            }
        })

        btn_search.setOnClickListener { searchForMovie(mQuery) }
        initRecycleView()
    }


    private fun initRecycleView() {
        mAdapter = object : GenericRecyclerViewAdapter<Movie>(null) {

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

    private fun searchForMovie(query: String) {
        progress_circular.visibility = View.VISIBLE
        val call = mService.searchForMovie(query)
        call.enqueue(object : Callback<MovieResponse> {

            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {

                progress_circular.visibility = View.GONE
                if (response.code() == 200) {
                    if (response.body()!!.results.isEmpty()) {
                        empty_list.visibility = View.VISIBLE
                    } else {
                        empty_list.visibility = View.GONE
                        mAdapter.setItems(response.body()!!.results)
                    }


                } else {
                    empty_list.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                progress_circular.visibility = View.GONE
                empty_list.visibility = View.VISIBLE
            }
        })

    }

}


