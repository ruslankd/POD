package com.example.pod.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pod.BuildConfig
import com.example.pod.repository.MarsResponseData
import com.example.pod.repository.NasaRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsViewModel(
        private val liveDataToObserve: MutableLiveData<MarsData> = MutableLiveData(),
        private val retrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl())
    : ViewModel() {

    private val marsCallback = object : Callback<MarsResponseData> {
        override fun onResponse(
                call: Call<MarsResponseData>,
                response: Response<MarsResponseData>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveDataToObserve.postValue(MarsData.Success(it))
                }
            } else {
                liveDataToObserve.postValue(
                        MarsData.Error(Error(response.message()))
                )
            }
        }

        override fun onFailure(call: Call<MarsResponseData>, t: Throwable) {
            liveDataToObserve.postValue(MarsData.Error(t))
        }
    }

    fun getLiveData(): LiveData<MarsData> = liveDataToObserve

    fun sendServerRequest(date: String) {
        liveDataToObserve.postValue(MarsData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY
        if(apiKey.isBlank()) {
            //
        } else {
            retrofitImpl.getMarsPhoto(date, apiKey, marsCallback)
        }
    }
}