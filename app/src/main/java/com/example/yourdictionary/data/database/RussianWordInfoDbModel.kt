package com.example.yourdictionary.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "russian_words")
data class RussianWordInfoDbModel(
    @PrimaryKey
    val textOnRussian: String,
    val timeRequest: Long,
    var textOnEnglish: String,
    var textOnFrench: String,
    var textOnGermany: String,
    val partOfSpeech: String,
    var meanings: String
)
