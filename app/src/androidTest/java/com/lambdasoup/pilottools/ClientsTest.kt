package com.lambdasoup.pilottools

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.lambdasoup.xpnet.Registry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@LargeTest
class ClientsTest {

    private val registry: Registry = mock()

    @get:Rule
    val activityRule = object: ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()

            val mockedUserModule = module {
                single(override = true) { registry }
            }
            loadKoinModules(listOf(mockedUserModule))
        }
    }

    @Test
    fun clientsEmpty() {
        verify(registry).listen { any() }
    }
}
