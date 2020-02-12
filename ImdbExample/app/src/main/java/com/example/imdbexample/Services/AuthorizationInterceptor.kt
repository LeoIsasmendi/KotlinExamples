package com.example.imdbexample.Services


import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val authorization: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestBuilder: Request.Builder = originalRequest.newBuilder()
            .header("Cache-Control", "no-cache")
            .header("Cache-Control", "no-store")
            .addHeader("Connection", "close")
            .header(AUTHORIZATION_HEADER, authorization)
            .method(originalRequest.method(), originalRequest.body())
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }

}