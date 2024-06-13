package com.example.moviesapp

import com.example.moviesapp.api.models.movies.MoviesBaseResponse
import com.example.moviesapp.api.models.movies.Result
import com.example.moviesapp.api.requests.MoviesServices
import com.example.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryTest {

    @Mock
    private lateinit var moviesServices: MoviesServices
    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        moviesRepository = MoviesRepository(moviesServices)
    }

    @Test
    fun `repoGetMovies calls getMovies service`() = runTest {
        val mockResults = listOf(
            Result(
                adult = false,
                backdrop_path = "/path.jpg",
                genre_ids = listOf(1, 2, 3),
                id = 1,
                original_language = "en",
                original_title = "Original Title",
                overview = "Overview",
                popularity = 9.8,
                poster_path = "/poster.jpg",
                release_date = "2021-01-01",
                title = "Title",
                video = false,
                vote_average = 8.5,
                vote_count = 1000
            )
        )
        val mockResponse = MoviesBaseResponse(
            page = 1,
            results = mockResults,
            total_pages = 10,
            total_results = 100
        )

        `when`(moviesServices.getMovies(false, false, "en", 1)).thenReturn(mockResponse)

        val result = moviesRepository.repoGetMovies(false, false, "en", 1)

        verify(moviesServices).getMovies(false, false, "en", 1)

        assertEquals(mockResponse, result)
    }
}
