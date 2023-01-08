package com.example.yourdictionary.domain.usecase

import com.example.yourdictionary.domain.repository.WordRepository

class LoadDataUseCase(
    private val repository: WordRepository
) {
    suspend operator fun invoke(lang: String, word: String) = repository.findWord(lang, word)

    suspend fun loadData(lang: String, word: String): Boolean{
        return repository.findWord(lang, word)
    }
}