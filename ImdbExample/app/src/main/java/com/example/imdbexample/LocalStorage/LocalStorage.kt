package com.example.imdbexample.LocalStorage

import android.content.Context
import android.content.SharedPreferences
import com.example.imdbexample.Models.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LocalStorage(mContext: Context) {

    private val preferencesName = "IMDB_SHARED_PREF"
    private val mKey = "hashString"
    private val defaultValue = "{}"
    private val gson = Gson()
    private var favorites: MutableMap<Int, Movie>
    private val sharedPref: SharedPreferences =
        mContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    init {
        favorites = fetchSavedData()
    }

    fun saveMovie(movie: Movie) {
        favorites[movie.id] = movie
        updatePreference(favorites)
    }

    fun deleteMovie(movieId: Int ) {
        favorites.remove(movieId)
        updatePreference(favorites)
    }

    private fun updatePreference(favorites: MutableMap<Int, Movie>) {
        val hashMapString = gson.toJson(favorites)
        sharedPref.edit().putString(mKey, hashMapString).apply()
    }

    private fun fetchSavedData(): MutableMap<Int, Movie> {
        val storedHashMapString: String? = sharedPref.getString(mKey, defaultValue)
        val type = object : TypeToken<HashMap<Int?, Movie?>?>() {}.type
        return gson.fromJson(storedHashMapString, type)
    }

    fun getMovies(): ArrayList<Movie> {
        return ArrayList(fetchSavedData().values)
    }

}