package com.marcobrenes.mockwebserverdemo

import io.appflate.restmock.RESTMockServer

class TestDemoApplication : DemoApplication() {
    override val baseUrl: String get() = RESTMockServer.getUrl()
}