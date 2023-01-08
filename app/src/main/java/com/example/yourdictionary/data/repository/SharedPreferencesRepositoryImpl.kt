package com.example.yourdictionary.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.yourdictionary.domain.repository.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl(private val application: Application)
    : SharedPreferencesRepository {
    override fun getCurrentStyle(): String {
        val settings: SharedPreferences =
            application.getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE)
        val chooseStyleMode = settings.getString(
            STYLE_MODE,
            "ru"
        )
        return chooseStyleMode?: throw RuntimeException("SharedPref style error")
    }

    override fun getCurrentLanguage(): String {
        val settings: SharedPreferences =
            application.getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE)
        val chooseStyleMode = settings.getString(
            LANGUAGE_MODE,
            "en"
        )
        return chooseStyleMode?: throw RuntimeException("SharedPref language error")
    }


    companion object{
        private const val LANGUAGE_MODE = "language_mode"
        private const val STYLE_MODE = "style_mode"
        private const val SETTINGS_STORAGE = "settings_storage"
    }
}