package com.example.imdbexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.imdbexample.Fragments.*

import com.example.imdbexample.Models.Movie

class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showMoviesFragment(savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        if (id == R.id.action_search) {
            showSearchFragment()
            return true
        }
        if (id == R.id.action_favorites) {
            showFavoritesFragment()
            return true
        }

        return super.onOptionsItemSelected(item)

    }


    private fun showMoviesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MoviesFragment.newInstance(), "moviesList")
                .commit()
        }
    }

    override fun onListFragmentInteraction(anObject: Any) {
        val item = anObject as Movie

        val fragment =
            MovieDetailsFragment.newInstance(item!!.id)
        supportFragmentManager
            .beginTransaction()
            // 2
            .replace(R.id.fragment_container, fragment, "movieDetails")
            // 3
            .addToBackStack(null)
            .commit()
    }

    private fun showSearchFragment() {
        val fragment = SearchFragment()
        supportFragmentManager
            .beginTransaction()
            // 2
            .replace(R.id.fragment_container, fragment, "search")
            // 3
            .addToBackStack(null)
            .commit()
    }

    private fun showFavoritesFragment() {
        val fragment = FavoritesFragment()
        supportFragmentManager
            .beginTransaction()
            // 2
            .replace(R.id.fragment_container, fragment, "favorites")
            // 3
            .addToBackStack(null)
            .commit()
    }

}
