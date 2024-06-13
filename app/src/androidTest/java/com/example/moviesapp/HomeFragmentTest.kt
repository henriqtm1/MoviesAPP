package com.example.moviesapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesapp.ui.home.HomeFragment
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest : KoinTest {

    @Test
    fun fragment_is_not_null() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.onFragment { fragment ->
            assertNotNull(fragment)
        }
    }

    @Test
    fun viewModel_is_initialized() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.onFragment { fragment ->
            assertNotNull(fragment.fetchHomeViewModel())
        }
    }

    @Test
    fun viewModel_vmGetMovies_called() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.onFragment { fragment ->
            val viewModel = fragment.fetchHomeViewModel()
            viewModel.vmGetMovies(false, false, "en", 1)
        }
    }
}