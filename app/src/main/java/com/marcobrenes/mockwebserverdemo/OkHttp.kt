package com.marcobrenes.mockwebserverdemo

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

interface OkHttp {
    companion object {
        val instance: OkHttpClient by lazy {
            OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.SECONDS)
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .build()
        }
    }
}