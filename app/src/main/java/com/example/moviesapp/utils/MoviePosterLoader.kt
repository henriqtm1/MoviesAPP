package com.example.moviesapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

private const val TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

object MoviePosterLoader {
    fun load(path: String, imageView: ImageView) {
        Glide.with(imageView)
            .load("$TMDB_POSTER_BASE_URL$path")
            .into(imageView)
    }
}
