package com.example.moviesapp.di

import com.example.moviesapp.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L
private const val ACCEPT = "Accept"
private const val CONTENT_TYPE_JSON = "application/json"
private const val AUTHORIZATION = "Authorization"
private const val TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMmVjNjM0NzY4MWYwZWJjMDY1M2I3NTQ2YWY4MTNjMCIsInN1YiI6IjYyMThjZGQ3MGU1OTdiMDAxYjllNWRmZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.5T_bXpluLPKhEcT7lRVGZ1LRvk3mejaJ3aG1VSq9TWM"

val RetrofitModule = module {
    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }
    single { GsonBuilder().create() }
    single { provideInterceptor() }
    single { retrofitHttpClient(get(), get()) }
    single(named("apiMovies")) { retrofitBuilder(BuildConfig.API_MOVIES, get()) }
}

private fun provideInterceptor(): Interceptor {
    return Interceptor { chain ->
        val request = chain.request().newBuilder().apply {
            addHeader(AUTHORIZATION, TOKEN)
            addHeader(ACCEPT, CONTENT_TYPE_JSON)
            addHeader("Content-Type", CONTENT_TYPE_JSON)
        }.build()
        chain.proceed(request)
    }
}

private fun retrofitBuilder(baseUrl: String, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

private fun retrofitHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder().apply {
        cache(cache)
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        addInterceptor(interceptor)
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }.build()
}
