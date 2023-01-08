package com.example.yourdictionary.domain.usecase

import com.example.yourdictionary.domain.repository.WordRepository

class GetRussianWordInfoListUseCase(
    private val repository: WordRepository
) {
    operator fun invoke() = repository.getRussianWordInfoList()
}