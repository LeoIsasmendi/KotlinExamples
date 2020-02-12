package com.example.imdbexample.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbexample.LocalStorage.LocalStorage
import com.example.imdbexample.Models.MovieDetailsResponse

import com.example.imdbexample.R
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
            }
        }
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
            list.adapter = MyMoviesRecyclerViewAdapterMovieDetails(result, null)
        }

    }

   }
