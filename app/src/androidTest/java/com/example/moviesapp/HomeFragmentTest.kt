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
        val lScenario = launchFragmentInContainer<HomeFragment>()
        lScenario.onFragment { aFragment ->
            assertNotNull(aFragment)
        }
    }

    @Test
    fun viewModel_is_initialized() {
        val lScenario = launchFragmentInContainer<HomeFragment>()
        lScenario.onFragment { aFragment ->
            assertNotNull(aFragment.fetchHomeViewModel())
        }
    }

    @Test
    fun viewModel_vmGetMovies_called() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.onFragment { aFragment ->
            val lViewmodel = aFragment.fetchHomeViewModel()
            lViewmodel.vmGetMovies(
                aIncludeAdult = false,
                aIncludeVideo = false,
                aLanguage = "en",
                aPage = 1
            )
        }
    }
}