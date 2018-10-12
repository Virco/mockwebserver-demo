package com.marcobrenes.mockwebserverdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.IdRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = application as DemoApplication
        val retrofit = Retrofit.Builder()
                .baseUrl(app.baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(OkHttp.instance)
                .build()

        val service = retrofit.create(GithubService::class.java)

        showFollowers(service, "octocat", R.id.followers)
        showFollowers(service, "virco", R.id.followers_1)
        showFollowers(service, "chiuki", R.id.followers_2)

    }

    private fun showFollowers(service: GithubService, username: String, @IdRes textViewId: Int) {
        val textView: TextView = findViewById(textViewId)
        service.getUser(username).enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(MainActivity::class.java.simpleName, t.message, t)
                textView.text = t::class.java.simpleName
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        textView.text = "${it.login}: ${it.followers}"
                    }
                } else {
                    textView.text = response.code().toString()
                }
            }
        })
    }
}
