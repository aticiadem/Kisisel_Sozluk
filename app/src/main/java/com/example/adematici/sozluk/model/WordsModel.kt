package com.example.adematici.sozluk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table")
data class WordsModel(
    @PrimaryKey(autoGenerate = true)
    val wordId: Int,
    @ColumnInfo(name = "wordEnglish")
    var wordEnglish: String,
    @ColumnInfo(name = "wordTurkish")
    var wordTurkish: String
)