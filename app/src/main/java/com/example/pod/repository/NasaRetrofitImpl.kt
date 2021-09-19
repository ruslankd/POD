package com.example.pod.repository

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NasaRetrofitImpl {
    private val baseUrl = "https://api.nasa.gov/"

    private val api by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .build().create(NasaAPI::class.java)
    }

    fun getPictureOfTheDay(date: String, apiKey: String, podCallback: Callback<PODServerResponseData>) {
        api.getPictureOfTheDay(date, apiKey).enqueue(podCallback)
    }

    fun getMarsPhoto(earthDate: String, apiKey: String, marsCallback: Callback<MarsResponseData>) {
        api.getMarsPhoto(earthDate, apiKey).enqueue(marsCallback)
    }


}