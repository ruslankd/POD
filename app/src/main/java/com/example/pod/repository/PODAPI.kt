package com.example.pod.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(
            @Query("date") date: String,
            @Query("api_key") apiKey: String
    ): Call<PODServerResponseData>

    //https://api.nasa.gov/planetary/apod?date=2020-02-01&api_key=YUOR_KEY
}