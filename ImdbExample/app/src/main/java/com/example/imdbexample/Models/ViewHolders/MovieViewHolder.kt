package com.example.imdbexample.Models.ViewHolders

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbexample.Fragments.GenericRecyclerViewAdapter
import com.example.imdbexample.Models.Movie
import com.example.imdbexample.R
import com.example.imdbexample.Services.Helper
import com.squareup.picasso.Picasso

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    GenericRecyclerViewAdapter.Binder<Movie> {

    var mPosterView: ImageView = itemView.findViewById(R.id.item_poster)

    override fun bind(item: Movie) {
        Picasso.get().load(Helper.BASE_URL_IMAGE + item.poster_path).into(mPosterView)
    }
}