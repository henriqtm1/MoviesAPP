package com.example.moviesapp.repository

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(
        val type: ApiErrorType,
        val message: String,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>
}
