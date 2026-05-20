package com.example.moviesapp.api.requests

import com.example.moviesapp.api.models.movies.MoviesBaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesServices {
    @GET("/3/discover/movie?sort_by=popularity.desc")
    suspend fun getMovies(
        @Query("include_adult") includeAdult: Boolean,
        @Query("include_video") includeVideo: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MoviesBaseResponse
}
