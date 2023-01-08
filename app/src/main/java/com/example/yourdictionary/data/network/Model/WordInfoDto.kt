package com.example.yourdictionary.data.network.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WordInfoDto(
    @SerializedName("text")
    @Expose
    val originalText: String? = null,
    @SerializedName("pos")
    @Expose
    val partOfSpeech:  String? = null,
    @SerializedName("ts")
    @Expose
    val transcription: String? = null,
    @SerializedName("tr")
    @Expose
    val translations: List<WordTranslationDto>? = null
)
