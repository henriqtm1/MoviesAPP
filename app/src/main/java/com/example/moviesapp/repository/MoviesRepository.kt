
package com.example.moviesapp.repository

import com.example.moviesapp.api.requests.MoviesServices

class MoviesRepository(private val aMoviesServices: MoviesServices) {
    suspend fun repoGetMovies(
        aIncludeAdult: Boolean,
        aIncludeVideo: Boolean,
        aLanguage: String,
        aPage: Int
    ) = aMoviesServices.getMovies(aIncludeAdult,aIncludeVideo,aLanguage,aPage)
}