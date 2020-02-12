package com.example.imdbexample.LocalStorage

import android.content.Context
import android.content.SharedPreferences
import com.example.imdbexample.Models.MovieDetailsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LocalStorage(mContext: Context) {

    private val preferencesName = "IMDB_SHARED_PREF"
    private val mKey = "hashString"
    private val gson = Gson()
    private var favorites: MutableMap<Int, MovieDetailsResponse>
    private val sharedPref: SharedPreferences =
        mContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    init {
        favorites = fetchSavedData()
    }

    fun saveMovie(movie: MovieDetailsResponse) {
        favorites.put(movie.id, movie)
        updatePreference(favorites)
    }

    fun deleteMovie(movie: MovieDetailsResponse) {
        favorites.remove(movie.id)
        updatePreference(favorites)
    }

    private fun updatePreference(favorites: MutableMap<Int, MovieDetailsResponse>) {
        val hashMapString = gson.toJson(favorites)
        sharedPref.edit().putString(mKey, hashMapString).apply()
    }

    private fun fetchSavedData(): MutableMap<Int, MovieDetailsResponse> {
        val storedHashMapString: String = sharedPref.getString(mKey, mKey)
        val type = object : TypeToken<HashMap<Int?, MovieDetailsResponse?>?>() {}.type
        return gson.fromJson(storedHashMapString, type)
    }

    fun getMovies(): ArrayList<MovieDetailsResponse> {
        return ArrayList(fetchSavedData().values)
    }

}