package com.example.adematici.sozluk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var kelimelerListe: ArrayList<Kelimeler>
    private lateinit var adapter: KelimelerAdapter
    private lateinit var vt: VeritabaniYardimcisi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Kişisel Sözlük"
        setSupportActionBar(toolbar)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)

        vt = VeritabaniYardimcisi(this)

        tumKelimeleriAl()


        fab.setOnClickListener {
            alertGoster()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val item = menu?.findItem(R.id.action_ara)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    fun alertGoster(){
        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_tasarim, null)
        val editTextIngilizce = tasarim.findViewById(R.id.editTextIngilizce) as EditText
        val editTextTurkce = tasarim.findViewById(R.id.editTextTurkce) as EditText

        val ad = AlertDialog.Builder(this)
        ad.setTitle("Kelime Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("Ekle"){ dialogInterface, i ->
            val kelime_ingilizce = editTextIngilizce.text.toString().trim()
            val kelime_turkce = editTextTurkce.text.toString().trim()

            Kelimelerdao().kelimeEkle(vt, kelime_ingilizce, kelime_turkce)
            tumKelimeleriAl()

            Toast.makeText(
                applicationContext,
                "${kelime_ingilizce} - ${kelime_turkce}",
                Toast.LENGTH_SHORT
            ).show()
        } // setPositiveButton

        ad.setNegativeButton("İptal"){ dialogInterface, i ->
        } // setNegativeButton

        ad.create().show()
    } // alertGoster

    override fun onQueryTextSubmit(query: String): Boolean {
        aramaYap(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        aramaYap(newText)
        return true
    }

    fun tumKelimeleriAl(){
        kelimelerListe = Kelimelerdao().tumKelimeler(vt)
        adapter = KelimelerAdapter(this, kelimelerListe, vt)
        rv.adapter = adapter
    }

    fun aramaYap(aramaKelime: String){
        kelimelerListe = Kelimelerdao().kelimeAra(vt, aramaKelime)
        adapter = KelimelerAdapter(this, kelimelerListe, vt)
        rv.adapter = adapter
    }

}