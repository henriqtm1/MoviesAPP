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
class HomeViewModelUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var moviesRepository: MoviesRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeViewModel = HomeViewModel(moviesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `vmGetMovies calls repoGetMovies`() = runTest {
        homeViewModel.vmGetMovies(false, false, "en", 1)

        Mockito.verify(moviesRepository).repoGetMovies(false, false, "en", 1)
    }

    @Test
    fun `vmGetMovies handles exception`() = runTest {
        Mockito.`when`(moviesRepository.repoGetMovies(false, false, "en", 1))
            .thenThrow(RuntimeException("Test exception"))

        homeViewModel.vmGetMovies(false, false, "en", 1)

       }
}
