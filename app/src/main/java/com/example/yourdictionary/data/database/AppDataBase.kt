package com.example.yourdictionary.data.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WordInfoDbModel::class, RussianWordInfoDbModel::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {

        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        application,
                        AppDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun wordInfoDao(): WordInfoDao
    abstract fun russianWordInfoDao(): RussianWordInfoDao
}