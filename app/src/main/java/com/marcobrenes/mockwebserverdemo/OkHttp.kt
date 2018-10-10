package com.marcobrenes.mockwebserverdemo

import okhttp3.OkHttpClient

interface OkHttp {
    companion object {
        val instance: OkHttpClient by lazy { OkHttpClient() }
    }
}