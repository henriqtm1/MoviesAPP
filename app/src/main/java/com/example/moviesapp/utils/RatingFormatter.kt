package com.example.moviesapp.utils

import java.text.DecimalFormat

object RatingFormatter {
    private val formatter = DecimalFormat("0.0")

    fun format(rating: Number): String {
        return formatter.format(rating)
    }
}
