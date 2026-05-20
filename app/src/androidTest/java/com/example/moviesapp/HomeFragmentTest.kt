package com.example.moviesapp

import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesapp.ui.home.HomeActivity
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Test
    fun home_fragment_is_loaded_by_home_activity() {
        val lScenario = ActivityScenario.launch(HomeActivity::class.java)
        lScenario.onActivity { aActivity ->
            val lNavHostFragment = aActivity.supportFragmentManager
                .findFragmentById(R.id.mobile_navigation) as NavHostFragment
            assertNotNull(lNavHostFragment.childFragmentManager.fragments.firstOrNull())
        }
    }

    @Test
    fun viewModel_is_initialized() {
        val lScenario = ActivityScenario.launch(HomeActivity::class.java)
        lScenario.onActivity { aActivity ->
            val lNavHostFragment = aActivity.supportFragmentManager
                .findFragmentById(R.id.mobile_navigation) as NavHostFragment
            val lHomeFragment = lNavHostFragment.childFragmentManager.fragments.firstOrNull()
            assertNotNull(lHomeFragment)
        }
    }
}
