package com.example.yourdictionary.data.network.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExampleOfUsage(
    @SerializedName("text")
    @Expose
    val text: String?,
    @SerializedName("tr")
    @Expose
    val exampleTranslationList: List<TranslationOfExample>? = null
)
