package com.example.moviesapp.di

import com.example.moviesapp.api.requests.MoviesServices
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    single<MoviesServices> { get<Retrofit>(named("apiMovies")).create(MoviesServices::class.java) }

}