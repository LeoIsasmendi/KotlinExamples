package com.example.imdbexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import com.example.imdbexample.LocalStorage.LocalStorage
import com.example.imdbexample.Models.Movie
import com.example.imdbexample.Models.MovieDetailsResponse
import com.example.imdbexample.Services.Helper
import com.example.imdbexample.Services.IMDBService
import com.example.imdbexample.Services.ServiceFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {

    private var isPosterFullscreen = false
    private var indexOfList = 0
    private lateinit var localStorage: LocalStorage
    private lateinit var mMovie: MovieDetailsResponse
    private val MOVIE_ID = "MOVIE_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        initViews()
    }

    override fun onStart() {
        super.onStart()
        getMovieDetails(intent.getIntExtra(MOVIE_ID, 0))
    }

    private fun initViews() {
        localStorage = LocalStorage(applicationContext)

        val imgSwitcher = findViewById<ImageSwitcher>(R.id.imgSwitcher)
        imgSwitcher?.setFactory {
            val imageView = ImageView(this)
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
                    val aMovie = Movie(
                        mMovie.poster_path,
                        mMovie.adult,
                        mMovie.overview,
                        mMovie.release_date,
                        emptyList(),
                        mMovie.id,
                        mMovie.original_title,
                        mMovie.original_language,
                        mMovie.title,
                        mMovie.backdrop_path,
                        mMovie.popularity,
                        0,
                        mMovie.video,
                        mMovie.vote_average,
                        false
                    )
                    localStorage.saveMovie(aMovie)
                } else {
                    localStorage.deleteMovie(mMovie.id)
                }
            }
        }

        item_poster.setOnClickListener {
            isPosterFullscreen = !isPosterFullscreen

            overview.visibility = if (isPosterFullscreen) View.GONE else View.VISIBLE
            details.visibility = if (isPosterFullscreen) View.GONE else View.VISIBLE


            val dimensionInDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                150f,
                resources.displayMetrics
            ).toInt()

            val newParams = item_poster.layoutParams
            newParams.width =
                if (isPosterFullscreen) ViewGroup.LayoutParams.MATCH_PARENT else dimensionInDp
            item_poster.layoutParams = newParams

        }
    }


    private fun toggleIndex(index: Int): Int {
        return if (index == 0) index + 1 else index - 1
    }

    private fun getResourceForSwitcher(index: Int): Int {
        return if (index == 0) R.drawable.ic_favorite_big_unselected_foreground else
            R.drawable.ic_favorite_big_selected_foreground
    }

    private fun getMovieDetails(id: Int) {
        val mService: IMDBService = ServiceFactory.IMDB.create()
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

                    //title?.text = mMovie.title
                    original_language?.text = mMovie.original_language
                    overview?.text = mMovie.overview
                    release_date?.text = mMovie.release_date

                    Picasso.get().load(Helper.BASE_URL_IMAGE + mMovie.poster_path)
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
