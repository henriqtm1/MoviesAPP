package com.example.moviesapp

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MoviesPage
import com.example.moviesapp.repository.ApiResult
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.ui.home.HomeViewModel
import com.example.moviesapp.ui.home.adapter.MovieDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    private lateinit var moviesRepository: MoviesRepository

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        homeViewModel = HomeViewModel(moviesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovies exposes paging data from repository`() = runTest {
        val expectedMovies = listOf(createMovie(1), createMovie(2))
        `when`(
            moviesRepository.getMovies(
                includeAdult = false,
                includeVideo = false,
                language = "en",
                page = 1
            )
        ).thenReturn(ApiResult.Success(createMoviesPage(1, 1, expectedMovies)))

        homeViewModel.loadMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 1
        )

        val differ = createDiffer()
        val collectJob = launch {
            homeViewModel.moviesPagingData.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        advanceUntilIdle()

        assertEquals(expectedMovies, differ.snapshot().items)
        verify(moviesRepository).getMovies(
            includeAdult = false,
            includeVideo = false,
            language = "en",
            page = 1
        )

        collectJob.cancel()
    }

    private fun createDiffer(): AsyncPagingDataDiffer<Movie> {
        return AsyncPagingDataDiffer(
            diffCallback = MovieDiffCallback,
            updateCallback = NoopListUpdateCallback,
            mainDispatcher = dispatcher,
            workerDispatcher = dispatcher
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

    private object NoopListUpdateCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
    }
}
