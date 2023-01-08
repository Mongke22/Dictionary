package com.example.yourdictionary.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.yourdictionary.data.database.AppDatabase
import com.example.yourdictionary.data.database.RussianWordInfoDbModel
import com.example.yourdictionary.data.mapper.WordMapper
import com.example.yourdictionary.data.network.ApiFactory
import com.example.yourdictionary.domain.model.WordInfo
import com.example.yourdictionary.domain.repository.WordRepository

class WordRepositoryImpl(
    private val application: Application
) : WordRepository {

    private val wordInfoDao = AppDatabase.getInstance(application).wordInfoDao()
    private val russianWordInfoDao = AppDatabase.getInstance(application).russianWordInfoDao()
    private val apiService = ApiFactory.apiService
    private val mapper = WordMapper()


    override fun getWordInfoList(): LiveData<List<WordInfo>> {
        return Transformations.map(wordInfoDao.getWordInfoList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override suspend fun getWordInfo(word: String): WordInfo {
        Log.i("word:",word)
        return mapper.mapDbModelToEntity(wordInfoDao.getWordInfo(word))

    }

    override fun getRussianWordInfoList(): LiveData<List<WordInfo>> {
        return Transformations.map(russianWordInfoDao.getWordInfoList()) {
            it.map {
                mapper.mapRussianDbModelToEntity(it)
            }
        }
    }

    override suspend fun getRussianWordInfo(word: String): WordInfo {
        return mapper.mapRussianDbModelToEntity(russianWordInfoDao.getWordInfo(word))

    }


    override suspend fun findWord(lang: String, word: String): Boolean {
        val lowerCaseWord = word.lowercase()
        var result = false
        try {
            val baseLanguage = lang.split("-")[FIRST_ENTRY]
            Log.i("searching - ", "$lowerCaseWord-$baseLanguage")
            if (wordInfoDao.checkWordExist(mapper.englishTermsToRussian(baseLanguage),
                    "$lowerCaseWord-$baseLanguage") > 0) {
                result = true
            } else {
                result = loadData(baseLanguage, lowerCaseWord)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private suspend fun loadData(lang: String, word: String): Boolean {
        val article = apiService.getTranslation(languages = "$lang-ru", text = word)
        wordInfoDao.insertWordInfo(mapper.mapDtoToDbModel(article, lang))
        // Ниже получаю пока только одно слово. Возможно, стоит добавить
        // получение всех вариантов переводов
        val russianTranslation = article.wordInfoDto?.get(FIRST_ENTRY)?.translations?.get(
            FIRST_ENTRY
        )?.translation ?: EMPTY_STRING
        Log.i("DB", "insert - $russianTranslation - $word")
        editRussianWord(russianTranslation, word, lang)
        return !article.wordInfoDto.isNullOrEmpty()
    }

    private suspend fun editRussianWord(
        textOnRussian: String,
        textOnForeignLanguage: String,
        language: String
    ) {
        if (russianWordInfoDao.checkWordExist(textOnRussian) == EXISTS) {
            var russianWord = russianWordInfoDao.getWordInfo(textOnRussian)
            when (language) {
                "en" -> {
                    russianWord.textOnEnglish += "$textOnForeignLanguage$PART_DELIMITER"
                }
                "fr" -> {
                    russianWord.textOnFrench += "$textOnForeignLanguage$PART_DELIMITER"
                }
                "de" -> {
                    russianWord.textOnGermany += "$textOnForeignLanguage$PART_DELIMITER"
                }
            }
            russianWordInfoDao.insertWordInfo(russianWord)

        } else {
            val russianArticle =
                apiService.getTranslation(languages = "ru-ru", text = textOnRussian)
            var meaningsOfTheWord = EMPTY_STRING
            var partsOfSpeech = russianArticle.wordInfoDto?.get(FIRST_ENTRY)?.partOfSpeech
                ?: "not found part of speech"
            russianArticle.wordInfoDto?.get(FIRST_ENTRY)?.translations?.let { meanings ->
                for (mean in meanings) {
                    meaningsOfTheWord += "${mean.translation}/"
                    partsOfSpeech += "/${mapper.englishTermsToRussian(mean.partOfSpeech)}"
                }
            }
            val wordToDb = RussianWordInfoDbModel(
                textOnRussian = textOnRussian,
                meanings = meaningsOfTheWord,
                partOfSpeech = partsOfSpeech,
                timeRequest = System.currentTimeMillis(),
                textOnGermany = EMPTY_STRING,
                textOnFrench = EMPTY_STRING,
                textOnEnglish = EMPTY_STRING
            )
            when (language) {
                "en" -> {
                    wordToDb.textOnEnglish += "$textOnForeignLanguage$PART_DELIMITER"
                }
                "fr" -> {
                    wordToDb.textOnFrench += "$textOnForeignLanguage$PART_DELIMITER"
                }
                "de" -> {
                    wordToDb.textOnGermany += "$textOnForeignLanguage$PART_DELIMITER"
                }
            }
            russianWordInfoDao.insertWordInfo(wordToDb)
        }
    }


    companion object {
        private const val EXISTS = 1
        private const val EMPTY_STRING = ""
        private const val FIRST_ENTRY = 0
        private const val PART_DELIMITER = "&"
    }
}