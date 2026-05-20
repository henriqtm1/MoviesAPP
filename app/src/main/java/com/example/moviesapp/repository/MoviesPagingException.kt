package com.example.moviesapp.repository

class MoviesPagingException(
    val type: ApiErrorType,
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause)
