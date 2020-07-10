package com.example.imdbexample.services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


enum class ServiceFactory(private val clazz: Class<*>) {

    IMDB(IMDBService::class.java);

    companion object {
        private var retrofitBuilder: Retrofit.Builder? = null
        private var httpClient: OkHttpClient? = null

        init {
            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.retryOnConnectionFailure(false)
            httpClientBuilder.connectTimeout(20, TimeUnit.SECONDS)
            httpClientBuilder.readTimeout(20, TimeUnit.SECONDS)
            httpClientBuilder.writeTimeout(20, TimeUnit.SECONDS)
            httpClient =
                httpClientBuilder.build()
            retrofitBuilder = Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
        }
    }

    fun <T> create(): T {
        val baseHttpClient = httpClient!!.newBuilder()
            .addInterceptor(QueryInterceptor())
            .build()
        return retrofitBuilder!!
            .callFactory(baseHttpClient)
            .build()
            .create(clazz) as T
    }

    fun <T> create(authorization: String?): T {
        var baseHttpClient = httpClient!!.newBuilder()
            .build()
        if (authorization != null) {
            baseHttpClient = httpClient!!.newBuilder()
                .addInterceptor(AuthorizationInterceptor(authorization))
                .build()
        }
        return retrofitBuilder!!
            .callFactory(baseHttpClient)
            .build()
            .create(clazz) as T
    }

}