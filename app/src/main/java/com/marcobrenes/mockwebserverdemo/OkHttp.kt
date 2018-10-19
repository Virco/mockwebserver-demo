package com.marcobrenes.mockwebserverdemo

import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface OkHttp {
    companion object {
        private var customClient: OkHttpClient? = null

        private val defaultClient: OkHttpClient by lazy {

            fun createCertificatePinner(): CertificatePinner {
                val hostName = "api.github.com"
                return CertificatePinner.Builder()
                        .add("sha256/y2HhTRXXLdmAF1esYBb/muQUl3BIBdmEB8jUvMrGc28=")
                        .add("sha256/k2v657xBsOVe1PQRwOsHsw3bsGT2VzIqz5K+59sNQws=")
                        .add("sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18=")
                        .build()
            }

            OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.SECONDS)
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .certificatePinner(createCertificatePinner())
                    .connectionSpecs(listOf(
                            ConnectionSpec.MODERN_TLS,
                            ConnectionSpec.COMPATIBLE_TLS
                    ))
                    .addInterceptor(HttpLoggingInterceptor())
                    .build()
        }


        var instance: OkHttpClient
            get() = customClient ?: defaultClient
            set(value) { customClient = value }
    }
}