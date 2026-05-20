package com.example.moviesapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesapp.ui.details.DetailsFragment
import com.example.moviesapp.ui.details.DetailsFragmentArgs
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentTest {

    @Test
    fun fragment_is_not_null() {
        val lArgs = DetailsFragmentArgs(
            image = "/poster.jpg",
            title = "Movie Title",
            rating = 8.5f,
            desc = "Movie description"
        ).toBundle()
        val lScenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs = lArgs)
        lScenario.onFragment { aFragment ->
            assertNotNull(aFragment)
        }
    }
}
