package com.marcobrenes.mockwebserverdemo

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MainActivityTest {
    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)
    @get:Rule
    val okHttpIdlingResourceRule = OkHttpIdlingResourceRule()
    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @Before
    fun setBaseUrl() {
        val app = ApplicationProvider.getApplicationContext() as TestDemoApplication
        app.baseUrl = mockWebServerRule.server.url("/").toString()
    }

    @Test
    fun followers() {
        mockWebServerRule.server.enqueue(MockResponse().setBody("""
            |{
            |   "login": "octocat",
            |   "followers": 1500
            |}
        """.trimMargin()))
        activityTestRule.launchActivity(null)

        onView(withId(R.id.followers))
                .check(matches(withText("octocat: 1500")))
    }
}
