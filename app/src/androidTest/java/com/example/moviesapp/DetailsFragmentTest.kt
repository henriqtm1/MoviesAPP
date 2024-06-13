package com.example.moviesapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesapp.ui.details.DetailsFragment
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class DetailsFragmentTest : KoinTest {

    @Test
    fun fragment_is_not_null() {
        val lScenario = launchFragmentInContainer<DetailsFragment>()
        lScenario.onFragment { aFragment ->
            assertNotNull(aFragment)
        }
    }
}