package com.example.yourdictionary.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yourdictionary.data.network.Model.WordInfoDto

@Dao
interface WordInfoDao {
    @Query("SELECT * FROM words_info WHERE textPlusLanguage == :word LIMIT 1")
    suspend fun getWordInfo(word: String): WordInfoDbModel

    @Query("SELECT * FROM words_info")
    fun getWordInfoList(): LiveData<List<WordInfoDbModel>>

    @Query("SELECT COUNT(*) FROM words_info WHERE textPlusLanguage ==:word AND originalLanguage ==:lang")
    suspend fun checkWordExist(lang: String, word: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordInfo(word: WordInfoDbModel)
}