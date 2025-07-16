package com.example.tmdbapp.di


import com.example.tmdbapp.BuildConfig
import com.example.tmdbapp.data.remote.TmdbApiService
import com.example.tmdbapp.data.repository.MovieRepositoryImpl
import com.example.tmdbapp.domain.repository.MovieRepository
import com.example.tmdbapp.ui.detail.DetailViewModel
import com.example.tmdbapp.ui.list.MovieListViewModel
import com.example.tmdbapp.ui.search.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java


val appModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .url(original.url)
                    .header("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(TmdbApiService.BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(TmdbApiService::class.java) }

    single<MovieRepository> { MovieRepositoryImpl(get()) }

    viewModel { MovieListViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { (itemId: Int, itemType: String) ->
        DetailViewModel(
            itemId,
            itemType,
            get()
        )
    }
}