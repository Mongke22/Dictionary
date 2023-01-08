package com.example.yourdictionary.data.network.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TranslationOfExample(
    @SerializedName("text")
    @Expose
    val text: String? = null
)
