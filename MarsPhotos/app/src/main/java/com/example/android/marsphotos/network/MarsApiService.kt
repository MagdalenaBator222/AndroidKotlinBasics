package com.example.android.marsphotos.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

// moshi instance for converting Json response to Kotlin objects
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// builds and creates a retrofit object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {
    @GET("photos") // get request from retrofit
    suspend fun getPhotos(): List<MarsPhoto> // when this method is invoked, the "photos" endpoint is appended to the base url
}

// singleton object - only one instance of the object is created
// to refer to the object, use its name directly
// below: object = initialisation of the retrofit service
// lazy instantiation: when object creation is purposefully delayed (until you actually need it)
// to avoid unnecessary use of computing resources

object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
