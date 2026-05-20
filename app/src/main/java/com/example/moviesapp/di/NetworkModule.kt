package com.example.moviesapp.di

import android.content.Context
import android.util.Log
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.api.requests.MoviesServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L
private const val CACHE_SIZE_BYTES = 10L * 1024 * 1024
private const val ACCEPT = "Accept"
private const val CURL_MOVIESAPP = "CURL_MoviesApp"
private const val CONTENT_TYPE_JSON = "application/json"
private const val AUTHORIZATION = "Authorization"
private const val BEARER_PREFIX = "Bearer "

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext aContext: Context): Cache {
        return Cache(aContext.cacheDir, CACHE_SIZE_BYTES)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val lRequest = chain.request().newBuilder().apply {
                val lAccessToken = BuildConfig.TMDB_ACCESS_TOKEN.trim()
                if (lAccessToken.isNotEmpty()) {
                    addHeader(AUTHORIZATION, lAccessToken.withBearerPrefix())
                }
                addHeader(ACCEPT, CONTENT_TYPE_JSON)
            }.build()
            chain.proceed(lRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(aCache: Cache, aInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            cache(aCache)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(aInterceptor)
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    redactHeader(AUTHORIZATION)
                    level = HttpLoggingInterceptor.Level.BODY
                })
                addInterceptor(
                    CurlInterceptor(
                        object : Logger {
                            override fun log(message: String) {
                                Log.v(CURL_MOVIESAPP, message)
                            }
                        }
                    )
                )
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(aClient: OkHttpClient, aGson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_MOVIES)
            .addConverterFactory(GsonConverterFactory.create(aGson))
            .client(aClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesServices(aRetrofit: Retrofit): MoviesServices {
        return aRetrofit.create(MoviesServices::class.java)
    }
}

private fun String.withBearerPrefix(): String {
    return if (startsWith(BEARER_PREFIX, ignoreCase = true)) this else "$BEARER_PREFIX$this"
}
