package com.example.imdbexample.services

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class QueryInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()
        val httpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter(
                API_KEY_QUERY_PARAM_KEY,
                Helper.API_KEY
            )
            .build()
        val requestBuilder = originalRequest.newBuilder()
            .header("Cache-Control", "no-cache")
            .header("Cache-Control", "no-store")
            .addHeader("Connection", "close")
            .url(httpUrl)
            .method(originalRequest.method(), originalRequest.body())
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    companion object {
        private const val API_KEY_QUERY_PARAM_KEY = "api_key"
    }
}