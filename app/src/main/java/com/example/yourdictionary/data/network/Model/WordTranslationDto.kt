package com.example.yourdictionary.data.network.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WordTranslationDto(
    @SerializedName("text")
    @Expose
    val translation: String? = null,
    @SerializedName("pos")
    @Expose
    val partOfSpeech: String? = null,
    @SerializedName("gen")
    @Expose
    val gender: String? = null,
    @SerializedName("syn")
    @Expose
    val  targetLanguageSynonyms: List<TargetLanguageSynonymDto>? = null,
    @SerializedName("mean")
    @Expose
    val  originalLanguageMeanings: List<OriginalLanguageMeanDto>? = null,
    @SerializedName("ex")
    @Expose
    val  examplesOfUsages: List<ExampleOfUsage>? = null
)
