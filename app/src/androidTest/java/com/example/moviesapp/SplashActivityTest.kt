package com.example.moviesapp

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesapp.ui.splash.SplashActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @Test
    fun activity_is_not_null() {
        val scenario = ActivityScenario.launch(SplashActivity::class.java)
        scenario.onActivity { activity ->
            assertNotNull(activity)
        }
    }
}
