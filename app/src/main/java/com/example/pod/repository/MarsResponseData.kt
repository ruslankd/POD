package com.example.pod.repository

import com.google.gson.annotations.SerializedName

data class MarsResponseData(
        @field:SerializedName("photos") val photos: List<Photo>
)