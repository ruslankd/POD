package com.example.pod.repository

import com.google.gson.annotations.SerializedName

data class Photo(
        @field:SerializedName("img_src") val imgSrc: String
)
