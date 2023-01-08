package com.example.yourdictionary.data.network.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WordInfoListDto(
    @SerializedName("def")
    @Expose
    val  wordInfoDto: List<WordInfoDto>? = null
)
