package com.example.moviesapp

import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesapp.ui.home.HomeActivity
import com.example.moviesapp.ui.home.HomeFragment
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Test
    fun home_fragment_is_loaded_by_home_activity() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        scenario.onActivity { activity ->
            val navHostFragment = activity.supportFragmentManager
                .findFragmentById(R.id.mobile_navigation) as NavHostFragment
            assertTrue(navHostFragment.childFragmentManager.fragments.firstOrNull() is HomeFragment)
        }
    }
}
