package com.example.adematici.sozluk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.adematici.sozluk.model.WordsModel

@Database(entities = [WordsModel::class], version = 1)
abstract class WordsDatabase: RoomDatabase() {

    abstract fun wordsDao(): WordsDao

    companion object {
        private var instance: WordsDatabase? = null

        fun getDatabase(context: Context): WordsDatabase? {
            if(instance == null){
                instance = Room.databaseBuilder(
                    context,
                    WordsDatabase::class.java,
                    "words.db"
                ).allowMainThreadQueries().build()
            }
            return instance
        }
    }

}