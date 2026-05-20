package com.example.moviesapp

import androidx.paging.PagingSource
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MoviesPage
import com.example.moviesapp.repository.ApiErrorType
import com.example.moviesapp.repository.ApiResult
import com.example.moviesapp.repository.MoviesPagingException
import com.example.moviesapp.repository.MoviesPagingSource
import com.example.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviesPagingSourceTest {

    @Mock
    private lateinit var mMoviesRepository: MoviesRepository

    private lateinit var mPagingSource: MoviesPagingSource

    @Before
    fun setUp() {
        mPagingSource = MoviesPagingSource(
            aMoviesRepository = mMoviesRepository,
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en"
        )
    }

    @Test
    fun `load returns first page with next key`() = runTest {
        val lMovies = listOf(createMovie(1))
        `when`(
            mMoviesRepository.getMovies(false, false, "en", 1)
        ).thenReturn(ApiResult.Success(createMoviesPage(1, 2, lMovies)))

        val lResult = mPagingSource.load(createRefreshParams())

        assertEquals(
            PagingSource.LoadResult.Page(
                data = lMovies,
                prevKey = null,
                nextKey = 2
            ),
            lResult
        )
        verify(mMoviesRepository).getMovies(false, false, "en", 1)
    }

    @Test
    fun `load returns last page without next key`() = runTest {
        val lMovies = listOf(createMovie(2))
        `when`(
            mMoviesRepository.getMovies(false, false, "en", 2)
        ).thenReturn(ApiResult.Success(createMoviesPage(2, 2, lMovies)))

        val lResult = mPagingSource.load(createAppendParams(key = 2))

        assertEquals(
            PagingSource.LoadResult.Page(
                data = lMovies,
                prevKey = 1,
                nextKey = null
            ),
            lResult
        )
        verify(mMoviesRepository).getMovies(false, false, "en", 2)
    }

    @Test
    fun `load returns typed exception when repository fails`() = runTest {
        `when`(
            mMoviesRepository.getMovies(false, false, "en", 1)
        ).thenReturn(ApiResult.Error(ApiErrorType.NO_CONNECTION, "No internet"))

        val lResult = mPagingSource.load(createRefreshParams())

        val lError = assertIs<PagingSource.LoadResult.Error<Int, Movie>>(lResult)
        val lException = assertIs<MoviesPagingException>(lError.throwable)
        assertEquals(ApiErrorType.NO_CONNECTION, lException.type)
        assertEquals("No internet", lException.message)
    }

    private fun createRefreshParams(): PagingSource.LoadParams.Refresh<Int> {
        return PagingSource.LoadParams.Refresh(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )
    }

    private fun createAppendParams(key: Int): PagingSource.LoadParams.Append<Int> {
        return PagingSource.LoadParams.Append(
            key = key,
            loadSize = 20,
            placeholdersEnabled = false
        )
    }

    private fun createMoviesPage(page: Int, totalPages: Int, movies: List<Movie>): MoviesPage {
        return MoviesPage(
            page = page,
            totalPages = totalPages,
            movies = movies
        )
    }

    private fun createMovie(id: Int): Movie {
        return Movie(
            id = id,
            title = "Movie Title $id",
            overview = "Overview",
            posterPath = "/poster-$id.jpg",
            voteAverage = 8.5
        )
    }
}
