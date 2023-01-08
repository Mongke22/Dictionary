package com.example.yourdictionary.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_info")
data class WordInfoDbModel (
    @PrimaryKey
    val textPlusLanguage: String,
    val timeRequest: Long,
    val originalLanguage: String,
    val partOfSpeech: String,
    val transcription: String,
    val gender: String,
    val russianTranslation: String
)
