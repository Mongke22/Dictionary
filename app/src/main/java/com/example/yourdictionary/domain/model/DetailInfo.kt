package com.example.yourdictionary.domain.model

data class DetailInfo(
    val language: String,
    val partOfSpeech: String,
    val gender: String,
    val translation: String,
    val transcription: String
)
