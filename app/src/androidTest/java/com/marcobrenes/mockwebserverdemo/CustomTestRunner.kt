package com.marcobrenes.mockwebserverdemo

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import io.appflate.restmock.android.AndroidLogger
import io.appflate.restmock.android.AndroidAssetsFileParser
import io.appflate.restmock.RESTMockServerStarter

class CustomTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        RESTMockServerStarter.startSync(AndroidAssetsFileParser(context), AndroidLogger())
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestDemoApplication::class.java.name, context)
    }
}