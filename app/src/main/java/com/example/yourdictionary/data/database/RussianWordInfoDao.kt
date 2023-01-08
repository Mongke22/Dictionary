package com.example.yourdictionary.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yourdictionary.data.network.Model.WordInfoDto

@Dao
interface RussianWordInfoDao {
    @Query("SELECT * FROM russian_words WHERE textOnRussian == :word LIMIT 1")
    suspend fun getWordInfo(word: String):RussianWordInfoDbModel

    @Query("SELECT * FROM russian_words")
    fun getWordInfoList(): LiveData<List<RussianWordInfoDbModel>>

    @Query("SELECT COUNT(*) FROM russian_words where textOnRussian == :word")
    suspend fun checkWordExist(word: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordInfo(word: RussianWordInfoDbModel)
}