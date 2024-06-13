
package com.example.moviesapp.di

import com.example.moviesapp.repository.MoviesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { MoviesRepository(get()) }
}