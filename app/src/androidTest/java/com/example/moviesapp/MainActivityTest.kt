package com.example.moviesapp

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesapp.ui.start.MainActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun activity_is_not_null() {
        val lScenario = ActivityScenario.launch(MainActivity::class.java)
        lScenario.onActivity { aActivity ->
            assertNotNull(aActivity)
        }
    }
}
