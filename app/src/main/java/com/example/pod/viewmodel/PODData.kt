package com.example.pod.viewmodel

import com.example.pod.repository.PODServerResponseData

sealed class PODData {
    data class Success(val serverResponseData: PODServerResponseData) : PODData()
    data class Error(val error: Throwable) : PODData()
    object Loading : PODData()
}
