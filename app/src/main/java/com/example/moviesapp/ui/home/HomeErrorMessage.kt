package com.example.moviesapp.ui.home

import com.example.moviesapp.R
import com.example.moviesapp.repository.ApiErrorType
import com.example.moviesapp.repository.MoviesPagingException

fun Throwable.toHomeErrorMessageRes(): Int {
    return (this as? MoviesPagingException)?.type?.toHomeErrorMessageRes()
        ?: R.string.txt_error_loading_movies
}

private fun ApiErrorType.toHomeErrorMessageRes(): Int {
    return when (this) {
        ApiErrorType.UNAUTHORIZED -> R.string.txt_error_loading_movies_unauthorized
        ApiErrorType.TIMEOUT -> R.string.txt_error_loading_movies_timeout
        ApiErrorType.NO_CONNECTION -> R.string.txt_error_loading_movies_no_connection
        ApiErrorType.SERVER -> R.string.txt_error_loading_movies_server
        ApiErrorType.UNKNOWN -> R.string.txt_error_loading_movies
    }
}
