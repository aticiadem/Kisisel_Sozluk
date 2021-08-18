package com.example.adematici.sozluk.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adematici.sozluk.*
import com.example.adematici.sozluk.adapter.WordsAdapter
import com.example.adematici.sozluk.databinding.ActivityMainBinding
import com.example.adematici.sozluk.databinding.WordUpdateDialogBinding
import com.example.adematici.sozluk.model.WordsModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var kelimelerListe: ArrayList<WordsModel>
    private lateinit var adapter: WordsAdapter
    private lateinit var vt: VeritabaniYardimcisi

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Kişisel Sözlük"
        setSupportActionBar(binding.toolbar)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        vt = VeritabaniYardimcisi(this)
        tumKelimeleriAl()

        binding.fab.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog(){
        val dialog = Dialog(this)
        val dialogBinding = WordUpdateDialogBinding.inflate(
            LayoutInflater.from(this)
        )
        dialog.setContentView(dialogBinding.root)

        dialogBinding.button.setText(R.string.add)

        dialogBinding.button.setOnClickListener {
            // add words
            //Kelimelerdao().kelimeEkle(vt, kelime_ingilizce, kelime_turkce)
            //tumKelimeleriAl()
        }

        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val item = menu?.findItem(R.id.actionSearch)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        aramaYap(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        aramaYap(newText)
        return true
    }

    private fun tumKelimeleriAl(){
        kelimelerListe = Kelimelerdao().tumKelimeler(vt)
        adapter = WordsAdapter(this, kelimelerListe)
        binding.recyclerView.adapter = adapter
    }

    private fun aramaYap(aramaKelime: String){
        kelimelerListe = Kelimelerdao().kelimeAra(vt, aramaKelime)
        adapter = WordsAdapter(this, kelimelerListe)
        binding.recyclerView.adapter = adapter
    }

}