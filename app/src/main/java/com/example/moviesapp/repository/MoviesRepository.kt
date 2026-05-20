package com.example.moviesapp.repository

import com.example.moviesapp.api.models.movies.Result
import com.example.moviesapp.api.requests.MoviesServices
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MoviesPage
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

private const val DEFAULT_MOVIES_ERROR_MESSAGE = "Unable to load movies."
private const val UNAUTHORIZED_ERROR_MESSAGE = "Unauthorized request."
private const val TIMEOUT_ERROR_MESSAGE = "The request timed out."
private const val NO_CONNECTION_ERROR_MESSAGE = "No internet connection."
private const val SERVER_ERROR_MESSAGE = "The server is unavailable."

interface MoviesRepository {
    suspend fun getMovies(
        includeAdult: Boolean,
        includeVideo: Boolean,
        language: String,
        page: Int
    ): ApiResult<MoviesPage>
}

class MoviesRepositoryImpl @Inject constructor(
    private val moviesServices: MoviesServices
) : MoviesRepository {
    override suspend fun getMovies(
        includeAdult: Boolean,
        includeVideo: Boolean,
        language: String,
        page: Int
    ): ApiResult<MoviesPage> {
        return try {
            val response = moviesServices.getMovies(
                includeAdult = includeAdult,
                includeVideo = includeVideo,
                language = language,
                page = page
            )
            ApiResult.Success(
                MoviesPage(
                    page = response.page,
                    totalPages = response.totalPages,
                    movies = response.results.map { it.toMovie() }
                )
            )
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            exception.toApiError()
        }
    }
}

private fun Result.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview.orEmpty(),
        posterPath = posterPath.orEmpty(),
        voteAverage = voteAverage
    )
}

private fun Exception.toApiError(): ApiResult.Error {
    return when (this) {
        is HttpException -> toHttpApiError()
        is SocketTimeoutException -> ApiResult.Error(
            type = ApiErrorType.TIMEOUT,
            message = TIMEOUT_ERROR_MESSAGE,
            cause = this
        )
        is UnknownHostException,
        is ConnectException,
        is NoRouteToHostException,
        is IOException -> ApiResult.Error(
            type = ApiErrorType.NO_CONNECTION,
            message = NO_CONNECTION_ERROR_MESSAGE,
            cause = this
        )
        else -> ApiResult.Error(
            type = ApiErrorType.UNKNOWN,
            message = message ?: DEFAULT_MOVIES_ERROR_MESSAGE,
            cause = this
        )
    }
}

private fun HttpException.toHttpApiError(): ApiResult.Error {
    return when (code()) {
        HttpURLConnection.HTTP_UNAUTHORIZED -> ApiResult.Error(
            type = ApiErrorType.UNAUTHORIZED,
            message = UNAUTHORIZED_ERROR_MESSAGE,
            cause = this
        )
        in 500..599 -> ApiResult.Error(
            type = ApiErrorType.SERVER,
            message = SERVER_ERROR_MESSAGE,
            cause = this
        )
        else -> ApiResult.Error(
            type = ApiErrorType.UNKNOWN,
            message = message ?: DEFAULT_MOVIES_ERROR_MESSAGE,
            cause = this
        )
    }
}
