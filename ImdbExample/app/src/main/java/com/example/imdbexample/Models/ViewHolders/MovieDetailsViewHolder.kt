package com.example.imdbexample.Models.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbexample.Fragments.GenericRecyclerViewAdapter
import com.example.imdbexample.Models.MovieDetailsResponse
import com.example.imdbexample.R
import com.example.imdbexample.Services.Helper
import com.squareup.picasso.Picasso

class MovieDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    GenericRecyclerViewAdapter.Binder<MovieDetailsResponse> {

    var mPosterView: ImageView = itemView.findViewById(R.id.item_poster)
    var mTitleView: TextView = itemView.findViewById(R.id.item_title)

    override fun bind(item: MovieDetailsResponse) {
        mTitleView.text = item.title
        Picasso.get().load(Helper.BASE_URL_IMAGE + item.poster_path).into(mPosterView)
    }
}