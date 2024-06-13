package com.example.moviesapp.api.requests

import com.example.moviesapp.api.models.movies.MoviesBaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesServices {
    @GET("/3/discover/movie?sort_by=popularity.desc")
    suspend fun getMovies(
        @Query("include_adult") aIncludeAdult: Boolean,
        @Query("include_video") aIncludeVideo: Boolean,
        @Query("language") aLanguage: String,
        @Query("page") aPage: Int
    ): MoviesBaseResponse
}