package com.example.pod.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pod.BuildConfig
import com.example.pod.repository.NasaRetrofitImpl
import com.example.pod.repository.PODServerResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error

class PODViewModel(
    private val liveDataToObserve: MutableLiveData<PODData> = MutableLiveData(),
    private val retrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl())
    : ViewModel() {

    private val podCallback = object : Callback<PODServerResponseData> {
        override fun onResponse(
                call: Call<PODServerResponseData>,
                response: Response<PODServerResponseData>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveDataToObserve.postValue(PODData.Success(it))
                }
            } else {
                liveDataToObserve.postValue(
                        PODData.Error(Error(response.message()))
                )
            }
        }

        override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
            liveDataToObserve.postValue(PODData.Error(t))
        }
    }

    fun getLiveData(): LiveData<PODData> = liveDataToObserve

    fun sendServerRequest(date: String) {
        liveDataToObserve.postValue(PODData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY
        if(apiKey.isBlank()){
            //
        } else {
            retrofitImpl.getPictureOfTheDay(date, apiKey, podCallback)
        }
    }
}