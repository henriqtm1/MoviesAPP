package com.example.moviesapp.model

data class MoviesPage(
    val page: Int,
    val totalPages: Int,
    val movies: List<Movie>
)
