package com.example.adematici.sozluk

import android.content.ContentValues

class Kelimelerdao {

    fun kelimeSil(vt: VeritabaniYardimcisi, kelime_id: Int){
        val db = vt.writableDatabase
        db.delete("kelimeler","kelime_id=?", arrayOf(kelime_id.toString()))
        db.close()
    }

    fun kelimeEkle(vt: VeritabaniYardimcisi, kelime_ingilizce: String, kelime_turkce: String){
        val db = vt.writableDatabase

        val values = ContentValues()
        values.put("kelime_ingilizce",kelime_ingilizce)
        values.put("kelime_turkce",kelime_turkce)

        db.insertOrThrow("kelimeler",null,values)
        db.close()
    }

    fun kelimeGuncelle(vt: VeritabaniYardimcisi, kelime_id: Int, kelime_ingilizce: String, kelime_turkce: String){
        val db = vt.writableDatabase

        val values = ContentValues()
        values.put("kelime_ingilizce",kelime_ingilizce)
        values.put("kelime_turkce",kelime_turkce)

        db.update("kelimeler",values,"kelime_id=?", arrayOf(kelime_id.toString()))
        db.close()
    }

    fun tumKelimeler(vt: VeritabaniYardimcisi) : ArrayList<Kelimeler> {
        val db = vt.writableDatabase
        val kelimelerListe = ArrayList<Kelimeler>()
        val c = db.rawQuery("SELECT * FROM kelimeler",null)

        while (c.moveToNext()){
            val kisi = Kelimeler(c.getInt(c.getColumnIndex("kelime_id")),
                c.getString(c.getColumnIndex("kelime_ingilizce")),
                c.getString(c.getColumnIndex("kelime_turkce")))
            kelimelerListe.add(kisi)
        }
        return kelimelerListe
    }

    fun kelimeAra(vt: VeritabaniYardimcisi, aramaKelime: String) : ArrayList<Kelimeler> {
        val db = vt.writableDatabase
        val kelimelerListe = ArrayList<Kelimeler>()
        val c = db.rawQuery("SELECT * FROM kelimeler WHERE kelime_ingilizce like '%$aramaKelime%'",null)

        while (c.moveToNext()){
            val kisi = Kelimeler(c.getInt(c.getColumnIndex("kelime_id")),
                c.getString(c.getColumnIndex("kelime_ingilizce")),
                c.getString(c.getColumnIndex("kelime_turkce")))
            kelimelerListe.add(kisi)
        }
        return kelimelerListe
    }

}