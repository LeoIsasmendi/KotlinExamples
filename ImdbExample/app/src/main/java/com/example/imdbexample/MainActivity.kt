package com.example.imdbexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbexample.Fragments.*
import com.example.imdbexample.LocalStorage.LocalStorage

import com.example.imdbexample.Models.Movie
import com.example.imdbexample.Models.MovieResponse
import com.example.imdbexample.Models.ViewHolders.MovieViewHolder
import com.example.imdbexample.Services.IMDBService
import com.example.imdbexample.Services.ServiceFactory
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {

    private val maxPageNumber = 100
    private var pageNumber = 1
    private var columnCount = 2
    private lateinit var mAdapter: GenericRecyclerViewAdapter<Movie>
    private lateinit var mOriginalList: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.action_menu, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { // Toast like print
                if (!searchView.isIconified()) {
                    searchView.setIconified(true)
                }
                myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean { // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                filterByTitle(s)
                return false
            }
        })

        return true
    }

    private fun filterByTitle(title: String?) {
        val filteredListItems: ArrayList<Movie> = ArrayList()
        val mTitle = title!!.toLowerCase(Locale.getDefault())

        if (mTitle.length == 0) {
            filteredListItems.addAll(mOriginalList)
        } else {
            for (aMovie in mAdapter.getItems()) {
                if (aMovie.title.toLowerCase(Locale.getDefault()).contains(mTitle)) {
                    filteredListItems.add(aMovie)
                }
            }
        }

        mAdapter.setItems(filteredListItems)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        if (id == R.id.action_search) {
            return true
        }
        if (id == R.id.action_favorites) {
            showFavorites()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showMovies() {
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

    override fun onListFragmentInteraction(anObject: Any) {
        val item = anObject as Movie
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("MOVIE_ID", item.id)
        startActivity(intent)
    }

    private fun showFavorites() {
        val localStorage = LocalStorage(this)
        val result = localStorage.getMovies()

        if (result.isEmpty()) {
            empty_list.visibility = View.VISIBLE
        } else {
            empty_list.visibility = View.GONE
            updateAdapter(result)
        }
    }

    private fun updateAdapter(newList: List<Movie>) {
        mOriginalList = newList
        mAdapter.setItems(newList)
    }


    private fun initRecycleView() {
        mAdapter = object : GenericRecyclerViewAdapter<Movie>(this) {
            override fun getLayoutId(position: Int, obj: Movie): Int {
                return R.layout.fragment_movies
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return MovieViewHolder(view)
            }

        }

        list.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(this)
            else -> GridLayoutManager(this, columnCount)
        }
        list.adapter = mAdapter

    }

    private fun getPageNumberText(): String {
        return getString(R.string.page_counter) + pageNumber.toString()
    }

    private fun fetchMovies() {
        val mService: IMDBService = ServiceFactory.IMDB.create()
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
                        updateAdapter(response.body()!!.results)
                    }
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                progress_circular?.visibility = View.GONE
            }
        })
    }

}
