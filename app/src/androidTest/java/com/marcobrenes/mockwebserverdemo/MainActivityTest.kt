package com.marcobrenes.mockwebserverdemo

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.InetAddress

class MainActivityTest {
    @get:Rule val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)
    @get:Rule val okHttpIdlingResourceRule = OkHttpIdlingResourceRule()
    @get:Rule val server = MockWebServer()

    private val OCTOCAT_BODY = """{"login":"octocat", "followers":1500}"""
    private val JESSE_BODY = """{"login":"swankjesse", "followers":2400}"""
    private val CHIUKI_BODY = """{"login":"chiuki", "followers":1000}"""

    @Before
    fun setBaseUrl() {
        val app = ApplicationProvider.getApplicationContext() as TestDemoApplication
        val localHost = createLocalHost()
        server.useHttps(localHost.sslSocketFactory(), false)
        val client = OkHttp.instance.newBuilder()
        client.sslSocketFactory(localHost.sslSocketFactory(), localHost.trustManager())
        OkHttp.instance = client.build()
        app.baseUrl = server.url("/").toString()
    }

    private fun createLocalHost(): HandshakeCertificates {
        // Generate a self-signed cert for the server to serve and the client to trust.
        val heldCertificate = HeldCertificate.Builder()
                .rsa2048()
                .commonName("localhost")
                .addSubjectAlternativeName(InetAddress.getByName("localhost").canonicalHostName)
                .build()

        return  HandshakeCertificates.Builder()
                .heldCertificate(heldCertificate)
                .addTrustedCertificate(heldCertificate.certificate())
                .addPlatformTrustedCertificates()
                .build()

    }

    @Test
    fun followers() {
        val dispatcher: Dispatcher = object: Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                request?.let {
                    val path = it.path
                    val parts = path.split("/")
                    val username = parts[parts.size - 1]
                    return MockResponse().setBody(
                            """{"login":"$username", "followers":${username.length}}"""
                    )
                } ?: return MockResponse()
            }
        }
        server.setDispatcher(dispatcher)
        activityTestRule.launchActivity(null)

        onView(withId(R.id.followers)).check(matches(withText("octocat: 7")))
        onView(withId(R.id.followers_1)).check(matches(withText("virco: 5")))
        onView(withId(R.id.followers_2)).check(matches(withText("chiuki: 6")))
    }
}
