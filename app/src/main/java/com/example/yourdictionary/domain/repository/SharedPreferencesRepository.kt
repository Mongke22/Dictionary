package com.example.yourdictionary.domain.repository

interface SharedPreferencesRepository {
    fun getCurrentStyle(): String
    fun getCurrentLanguage(): String
}