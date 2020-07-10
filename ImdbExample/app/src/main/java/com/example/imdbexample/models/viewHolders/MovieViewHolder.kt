package com.example.imdbexample.models.viewHolders

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbexample.fragments.GenericRecyclerViewAdapter
import com.example.imdbexample.models.Movie
import com.example.imdbexample.R
import com.example.imdbexample.services.Helper
import com.squareup.picasso.Picasso

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    GenericRecyclerViewAdapter.Binder<Movie> {

    var mPosterView: ImageView = itemView.findViewById(R.id.item_poster)

    override fun bind(item: Movie) {
        Picasso.get().load(Helper.BASE_URL_IMAGE + item.poster_path).into(mPosterView)
    }
}