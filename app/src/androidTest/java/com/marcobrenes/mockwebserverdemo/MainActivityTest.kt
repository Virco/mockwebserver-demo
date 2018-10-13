package com.marcobrenes.mockwebserverdemo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.RequestsVerifier
import io.appflate.restmock.utils.RequestMatchers.pathEndsWith
import io.appflate.restmock.utils.RequestMatchers.pathStartsWith
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)
    @get:Rule val okHttpIdlingResourceRule = OkHttpIdlingResourceRule()

    @Test fun followers() {
        RESTMockServer.reset()
        RESTMockServer.whenGET(pathEndsWith("octocat"))
                .thenReturnFile("users/octocat.json")
        activityTestRule.launchActivity(null)
        onView(withId(R.id.followers)).check(matches(withText("octocat: 1500")))

        RequestsVerifier.verifyGET(pathStartsWith("/users/octocat")).invoked()
    }

    @Test fun status404() {
        RESTMockServer.whenGET(pathEndsWith("octocat"))
                .thenReturnEmpty(404)
        activityTestRule.launchActivity(null)
        onView(withId(R.id.followers))
                .check(matches(withText("404")))
    }
}
