package com.example.moviesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val mInstantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mMoviesRepository: MoviesRepository

    private lateinit var mHomeViewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mHomeViewModel = HomeViewModel(mMoviesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `vmGetMovies calls repoGetMovies`() = runTest {
        mHomeViewModel.vmGetMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        )

        Mockito.verify(mMoviesRepository).repoGetMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        )
    }

    @Test
    fun `vmGetMovies handles exception`() = runTest {
        Mockito.`when`(mMoviesRepository.repoGetMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        ))
            .thenThrow(RuntimeException("Test exception"))

        mHomeViewModel.vmGetMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = "en",
            aPage = 1
        )

       }
}
