package com.example.yourdictionary.domain.usecase

import com.example.yourdictionary.domain.repository.WordRepository

class GetRussianWordInfoUseCase(
    private val repository: WordRepository
) {
    suspend operator fun invoke(word: String) = repository.getRussianWordInfo(word)
}