package com.example.adematici.sozluk

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VeritabaniYardimcisi (context: Context) : SQLiteOpenHelper(context,"kelimeler.sqlite",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE kelimeler(kelime_id INTEGER PRIMARY KEY AUTOINCREMENT, kelime_ingilizce Text, kelime_turkce TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS kelimeler")
        onCreate(db)
    }

}