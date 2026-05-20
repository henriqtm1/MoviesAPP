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
    private lateinit var mMoviesServices: MoviesServices
    private lateinit var mMoviesRepository: MoviesRepository

    @Before
    fun setUp() {
        mMoviesRepository = MoviesRepositoryImpl(mMoviesServices)
    }

    @Test
    fun `getMovies returns mapped page when service succeeds`() = runTest {
        val lMockResponse = MoviesBaseResponse(
            page = 2,
            results = listOf(createResult()),
            totalPages = 10,
            totalResults = 100
        )

        `when`(
            mMoviesServices.getMovies(
                aIncludeAdult = false,
                aIncludeVideo = false,
                aLanguage = "en",
                aPage = 2
            )
        ).thenReturn(lMockResponse)

        val lResult = mMoviesRepository.getMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 2
        )

        verify(mMoviesServices).getMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 2
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
            lResult
        )
    }

    @Test
    fun `getMovies returns error when service fails`() = runTest {
        `when`(
            mMoviesServices.getMovies(
                aIncludeAdult = false,
                aIncludeVideo = false,
                aLanguage = "en",
                aPage = 1
            )
        ).thenThrow(RuntimeException("Test exception"))

        val lResult = mMoviesRepository.getMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        )

        val lError = assertIs<ApiResult.Error>(lResult)
        assertEquals(ApiErrorType.UNKNOWN, lError.type)
        assertEquals("Test exception", lError.message)
    }

    @Test
    fun `getMovies returns unauthorized error when service returns 401`() = runTest {
        stubServiceFailure(createHttpException(401))

        val lResult = mMoviesRepository.getMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        )

        val lError = assertIs<ApiResult.Error>(lResult)
        assertEquals(ApiErrorType.UNAUTHORIZED, lError.type)
    }

    @Test
    fun `getMovies returns timeout error when request times out`() = runTest {
        stubServiceFailure(SocketTimeoutException("timeout"))

        val lResult = mMoviesRepository.getMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        )

        val lError = assertIs<ApiResult.Error>(lResult)
        assertEquals(ApiErrorType.TIMEOUT, lError.type)
    }

    @Test
    fun `getMovies returns no connection error when host cannot be resolved`() = runTest {
        stubServiceFailure(UnknownHostException("no internet"))

        val lResult = mMoviesRepository.getMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        )

        val lError = assertIs<ApiResult.Error>(lResult)
        assertEquals(ApiErrorType.NO_CONNECTION, lError.type)
    }

    private suspend fun stubServiceFailure(aException: Exception) {
        `when`(
            mMoviesServices.getMovies(
                aIncludeAdult = false,
                aIncludeVideo = false,
                aLanguage = "en",
                aPage = 1
            )
        ).thenAnswer { throw aException }
    }

    private fun createHttpException(aStatusCode: Int): HttpException {
        val lErrorBody = "{}".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<MoviesBaseResponse>(aStatusCode, lErrorBody))
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
