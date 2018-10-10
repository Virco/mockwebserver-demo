package com.marcobrenes.mockwebserverdemo

import android.app.Application

open class DemoApplication : Application() {
    open val baseUrl = "https://api.github.com"
}