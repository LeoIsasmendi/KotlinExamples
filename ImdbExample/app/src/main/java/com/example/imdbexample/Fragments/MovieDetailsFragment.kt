package com.example.imdbexample.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.example.imdbexample.LocalStorage.LocalStorage
import com.example.imdbexample.Models.MovieDetailsResponse

import com.example.imdbexample.R
import com.example.imdbexample.Services.Helper
import com.example.imdbexample.Services.IMDBService
import com.example.imdbexample.Services.ServiceFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsFragment : Fragment() {

    private var indexOfList = 0
    private lateinit var localStorage: LocalStorage
    private lateinit var mMovie: MovieDetailsResponse

    companion object {
        private const val MOVIE_ID = "movieId"

        fun newInstance(id: Int): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = bundleOf(MOVIE_ID to id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        localStorage = LocalStorage(activity!!.applicationContext)

        val imgSwitcher = view.findViewById<ImageSwitcher>(R.id.imgSwitcher)
        imgSwitcher?.setFactory {
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

            imageView
        }

        imgSwitcher?.setImageResource(getResourceForSwitcher(indexOfList))

        imgSwitcher?.setOnClickListener {

            indexOfList = toggleIndex(indexOfList)
            imgSwitcher.setImageResource(getResourceForSwitcher(indexOfList))

            if (::mMovie.isInitialized) {
                if (indexOfList == 1) {
                    localStorage.saveMovie(mMovie)
                } else {
                    localStorage.deleteMovie(mMovie)
                }
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun toggleIndex(index: Int): Int {
        return if (index == 0) index + 1 else index - 1
    }

    private fun getResourceForSwitcher(index: Int): Int {
        return if (index == 0) R.drawable.ic_favorite_big_unselected_foreground else
            R.drawable.ic_favorite_big_selected_foreground
    }

    override fun onStart() {
        super.onStart()
        getMovieDetails(arguments!!.getInt(MOVIE_ID))
    }


    private fun getMovieDetails(id: Int) {
        val mService: IMDBService = ServiceFactory.IMDB.create(Helper.API_KEY)
        val call = mService.getMovieDetails(id)

        progress_circular.visibility = View.VISIBLE
        call.enqueue(object : Callback<MovieDetailsResponse> {

            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                progress_circular.visibility = View.GONE

                if (response.code() == 200) {
                    mMovie = response.body() as MovieDetailsResponse

                    title?.text = mMovie.title
                    original_language?.text = mMovie.original_language
                    overview?.text = mMovie.overview
                    release_date?.text = mMovie.release_date
                    popularity?.text = mMovie.popularity.toString()

                    Picasso.get().load(Helper.BASE_URL_IMAGE + mMovie?.poster_path)
                        .error(R.drawable.ic_placeholder)
                        .into(item_poster)
                }
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                progress_circular.visibility = View.GONE
                Picasso.get().load(R.drawable.ic_placeholder)
            }

        })

    }

}
