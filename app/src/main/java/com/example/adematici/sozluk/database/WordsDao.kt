package com.example.adematici.sozluk.database

import androidx.room.*
import com.example.adematici.sozluk.model.WordsModel

@Dao
interface WordsDao {

    @Insert
    fun addWord(word: WordsModel)

    @Query("SELECT * FROM words_table")
    fun getAllWords(): List<WordsModel>

    @Update
    fun updateWord(word: WordsModel)

    @Delete
    fun deleteWord(word: WordsModel)

}