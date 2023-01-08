package com.example.yourdictionary.domain.repository

import androidx.lifecycle.LiveData
import com.example.yourdictionary.domain.model.WordInfo

interface WordRepository {

    fun getWordInfoList(): LiveData<List<WordInfo>>

    suspend fun getWordInfo(word: String): WordInfo

    fun getRussianWordInfoList(): LiveData<List<WordInfo>>

    suspend fun getRussianWordInfo(word: String): WordInfo

    suspend fun findWord(lang: String, word: String): Boolean
}