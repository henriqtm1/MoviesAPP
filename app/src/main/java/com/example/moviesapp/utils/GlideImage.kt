package com.example.moviesapp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideImage {

    companion object {
        fun GlideImageTransform(aContext: Context, aPathImage: String, aImageView: ImageView) {
            Glide.with(aContext)
                .load("https://image.tmdb.org/t/p/w500${aPathImage}")
                .into(aImageView)
        }
    }
}