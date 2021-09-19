package com.example.pod.viewmodel

import com.example.pod.repository.MarsResponseData

sealed class MarsData {
    data class Success(val serverResponseData: MarsResponseData) : MarsData()
    data class Error(val error: Throwable) : MarsData()
    object Loading : MarsData()
}