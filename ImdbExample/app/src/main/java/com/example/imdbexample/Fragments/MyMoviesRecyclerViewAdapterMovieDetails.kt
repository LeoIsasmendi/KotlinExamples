package com.example.imdbexample.Fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.imdbexample.R


import com.example.imdbexample.Fragments.MoviesFragment.OnListFragmentInteractionListener

import com.example.imdbexample.Models.Movie
import com.example.imdbexample.Models.MovieDetailsResponse
import com.example.imdbexample.Services.Helper
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_movies.view.*


class MyMoviesRecyclerViewAdapterMovieDetails(

    private val mValues: List<MovieDetailsResponse>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMoviesRecyclerViewAdapterMovieDetails.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as MovieDetailsResponse
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            //mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.mTitleView.text = item.title

        Picasso.get().load(Helper.BASE_URL_IMAGE + item.poster_path).into(holder.mPosterView)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val mPosterView: ImageView = mView.item_poster
        val mTitleView: TextView = mView.item_title

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}
