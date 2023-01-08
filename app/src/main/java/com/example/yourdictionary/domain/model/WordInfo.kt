package com.example.yourdictionary.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordInfo(
    var timeRequest: Long,
    var text: String,
    var originalLanguage: String,
    var partOfSpeech: String,
    var transcription: String,
    var gender: String,
    var translations: String
): Parcelable
