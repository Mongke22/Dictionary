package com.example.yourdictionary.domain.usecase

import com.example.yourdictionary.domain.repository.WordRepository

class GetWordInfoUseCase(
    private val repository: WordRepository
) {
    suspend operator fun invoke(word: String) = repository.getWordInfo(word)
}