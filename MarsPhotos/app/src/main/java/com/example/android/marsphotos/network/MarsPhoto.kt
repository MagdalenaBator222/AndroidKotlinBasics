package com.example.android.marsphotos.network

import com.squareup.moshi.Json

data class MarsPhoto(
    // @Json annotation -> maps the variable imgSrcUrl to the Json attribute img_src
    // camel case in Kotlin, just a convention for naming variables
    val id: String, @Json(name = "img_src") val imgSrcUrl: String
)