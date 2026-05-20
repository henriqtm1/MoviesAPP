package com.example.moviesapp

import com.example.moviesapp.api.models.movies.MoviesBaseResponse
import com.example.moviesapp.api.models.movies.Result
import com.example.moviesapp.api.requests.MoviesServices
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MoviesPage
import com.example.moviesapp.repository.ApiErrorType
import com.example.moviesapp.repository.ApiResult
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.repository.MoviesRepositoryImpl
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryTest {

    @Mock
    private lateinit var moviesServices: MoviesServices
    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        moviesRepository = MoviesRepositoryImpl(moviesServices)
    }

    @Test
    fun `getMovies returns mapped page when service succeeds`() = runTest {
        val mockResponse = MoviesBaseResponse(
            page = 2,
            results = listOf(createResult()),
            totalPages = 10,
            totalResults = 100
        )

        `when`(
            moviesServices.getMovies(
                includeAdult = false,
                includeVideo = false,
                language = "en",
                page = 2
            )
        ).thenReturn(mockResponse)

        val result = moviesRepository.getMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 2
        )

        verify(moviesServices).getMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 2
        )

        assertEquals(
            ApiResult.Success(
                MoviesPage(
                    page = 2,
                    totalPages = 10,
                    movies = listOf(
                        Movie(
                            id = 1,
                            title = "Title",
                            overview = "Overview",
                            posterPath = "/poster.jpg",
                            voteAverage = 8.5
                        )
                    )
                )
            ),
            result
        )
    }

    @Test
    fun `getMovies returns error when service fails`() = runTest {
        `when`(
            moviesServices.getMovies(
                includeAdult = false,
                includeVideo = false,
                language = "en",
                page = 1
            )
        ).thenThrow(RuntimeException("Test exception"))

        val result = moviesRepository.getMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 1
        )

        val error = assertIs<ApiResult.Error>(result)
        assertEquals(ApiErrorType.UNKNOWN, error.type)
        assertEquals("Test exception", error.message)
    }

    @Test
    fun `getMovies returns unauthorized error when service returns 401`() = runTest {
        stubServiceFailure(createHttpException(401))

        val result = moviesRepository.getMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 1
        )

        val error = assertIs<ApiResult.Error>(result)
        assertEquals(ApiErrorType.UNAUTHORIZED, error.type)
    }

    @Test
    fun `getMovies returns timeout error when request times out`() = runTest {
        stubServiceFailure(SocketTimeoutException("timeout"))

        val result = moviesRepository.getMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 1
        )

        val error = assertIs<ApiResult.Error>(result)
        assertEquals(ApiErrorType.TIMEOUT, error.type)
    }

    @Test
    fun `getMovies returns no connection error when host cannot be resolved`() = runTest {
        stubServiceFailure(UnknownHostException("no internet"))

        val result = moviesRepository.getMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 1
        )

        val error = assertIs<ApiResult.Error>(result)
        assertEquals(ApiErrorType.NO_CONNECTION, error.type)
    }

    private suspend fun stubServiceFailure(exception: Exception) {
        `when`(
            moviesServices.getMovies(
                includeAdult = false,
                includeVideo = false,
                language = "en",
                page = 1
            )
        ).thenAnswer { throw exception }
    }

    private fun createHttpException(statusCode: Int): HttpException {
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<MoviesBaseResponse>(statusCode, errorBody))
    }

    private fun createResult(): Result {
        return Result(
            adult = false,
            backdropPath = "/path.jpg",
            genreIds = listOf(1, 2, 3),
            id = 1,
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Overview",
            popularity = 9.8,
            posterPath = "/poster.jpg",
            releaseDate = "2021-01-01",
            title = "Title",
            video = false,
            voteAverage = 8.5,
            voteCount = 1000
        )
    }
}
