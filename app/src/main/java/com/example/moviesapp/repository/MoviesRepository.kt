
package com.example.moviesapp.repository

import com.example.moviesapp.api.requests.MoviesServices

class MoviesRepository(private val moviesServices: MoviesServices) {
    suspend fun repoGetMovies(
        aIncludeAdult: Boolean,
        aIncludeVideo: Boolean,
        aLanguage: String,
        aPage: Int
    ) = moviesServices.getMovies(aIncludeAdult,aIncludeVideo,aLanguage,aPage)
}