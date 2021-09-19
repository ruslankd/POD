package com.example.pod.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(
            @Query("date") date: String,
            @Query("api_key") apiKey: String
    ): Call<PODServerResponseData>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsPhoto(
            @Query("earth_date") earthDate: String,
            @Query("api_key") apiKey: String
    ): Call<MarsResponseData>
}