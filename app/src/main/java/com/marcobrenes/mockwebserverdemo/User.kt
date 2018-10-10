package com.marcobrenes.mockwebserverdemo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class User(
        val login: String = "",
        val id: String = "",
        val name: String = "",
        val followers: String = "",
        val default: String = "This is a default type")