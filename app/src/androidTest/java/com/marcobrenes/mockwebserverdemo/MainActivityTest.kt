package com.marcobrenes.mockwebserverdemo

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class MainActivityTest {
    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)
    @get:Rule
    val okHttpIdlingResourceRule = OkHttpIdlingResourceRule()
    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    private val OCTOCAT_BODY = """
            |{
            |   "login": "octocat",
            |   "followers": 1500
            |}
        """.trimMargin()

    @Before
    fun setBaseUrl() {
        val app = ApplicationProvider.getApplicationContext() as TestDemoApplication
        app.baseUrl = mockWebServerRule.server.url("/").toString()
    }

    @Test
    fun followers() {
        mockWebServerRule.server.enqueue(MockResponse().setBody(OCTOCAT_BODY))
        activityTestRule.launchActivity(null)

        onView(withId(R.id.followers))
                .check(matches(withText("octocat: 1500")))
    }

    @Test
    fun status404() {
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(404))
        activityTestRule.launchActivity(null)
        onView(withId(R.id.followers))
                .check(matches(withText("404")))
    }

    @Test
    fun malformedJson() {
        mockWebServerRule.server.enqueue(MockResponse().setBody("Jason"))
        activityTestRule.launchActivity(null)
        onView(withId(R.id.followers))
                .check(matches(withText("JsonEncodingException")))
    }

    @Test
    fun timeout() {
        mockWebServerRule.server.enqueue(MockResponse().setBody(OCTOCAT_BODY)
                .throttleBody(1, 1, TimeUnit.SECONDS))
        activityTestRule.launchActivity(null)
        onView(withId(R.id.followers))
                .check(matches(withText("SocketTimeoutException")))
    }
}
